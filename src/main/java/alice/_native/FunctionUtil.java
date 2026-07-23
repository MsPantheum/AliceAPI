package alice._native;

import alice.Platform;
import alice.injector.SymbolLookup;
import alice.log.Logger;
import alice.util.FileUtil;
import alice.util.ProcReader;
import com.kichik.pecoff4j.PE;
import com.kichik.pecoff4j.io.PEParser;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fornwall.jelf.ElfFile;
import net.fornwall.jelf.ElfSection;
import net.fornwall.jelf.ElfSectionHeader;
import net.fornwall.jelf.ElfSymbol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FunctionUtil {

    public static final class FunctionInfo {
        public final String name;
        public final long start;
        public final long end;

        public FunctionInfo(String name, long start, long end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(start);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FunctionInfo) {
                return this.start == ((FunctionInfo) obj).start;
            }
            return false;
        }
    }

    private static final Map<String, FunctionInfo> named_functions = new HashMap<>();
    private static final Long2ObjectOpenHashMap<FunctionInfo> all_functions = new Long2ObjectOpenHashMap<>();
    private static final Set<String> scannedLibraries = new HashSet<>();

    static {
        scanFunctions();
    }

    public static FunctionInfo getFunctionInfo(String name) {
        FunctionInfo info = named_functions.get(name);
        if (info == null) {
            scanFunctions();
            info = named_functions.get(name);
        }
        if (info == null && Platform.win32) {
            info = cacheWindowsExport(name);
        }
        return info;
    }

    public static FunctionInfo getFunctionInfo(long address) {
        FunctionInfo info = all_functions.get(address);
        if (info == null) {
            scanFunctions();
            info = all_functions.get(address);
        }
        return info;
    }

    private static synchronized void scanFunctions() {
        Map<String, ArrayList<ProcReader.MemoryMapping>> mappings = ProcReader.parseProcMaps();
        for (Map.Entry<String, ArrayList<ProcReader.MemoryMapping>> entry : mappings.entrySet()) {
            String library = entry.getKey();
            if (scannedLibraries.contains(library)) {
                continue;
            }
            if (!FileUtil.exists(library) || FileUtil.isDirectory(library)) {
                scannedLibraries.add(library);
                continue;
            }
            long base = SymbolLookup.getBase(entry.getValue());
            if (base == 0) {
                scannedLibraries.add(library);
                continue;
            }
            scannedLibraries.add(library);
            Logger.MAIN.info("Scanning ".concat(library).concat("."));
            if (Platform.win32) {
                try {
                    PE pe = PEParser.parse(library);
                    byte[] pdataBytes = pe.getImageData().getExceptionTable();
                    if (pdataBytes != null) {
                        ByteBuffer pdataBuf = ByteBuffer.wrap(pdataBytes).order(ByteOrder.nativeOrder());
                        int totalEntries = pdataBytes.length / 12;
                        for (int i = 0; i < totalEntries; i++) {
                            long start = base + (pdataBuf.getInt() & 0xffffffffL);
                            long end = base + (pdataBuf.getInt() & 0xffffffffL);
                            pdataBuf.getInt();// Skip unwindInfoRVA

                            FunctionInfo function = new FunctionInfo(null, start, end);
                            all_functions.put(start, function);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (ProcReader.isElf(library)) {
                    try {
                        ElfFile elf = ElfFile.from(Files.newInputStream(Paths.get(library)));
                        ElfSymbol[] symbols;
                        if (elf.getSymbolTableSection() != null) {
                            symbols = elf.getSymbolTableSection().symbols;
                            readElfSymbols(base, symbols);
                        }
                        if (elf.getDynamicSymbolTableSection() != null) {
                            symbols = elf.getDynamicSymbolTableSection().symbols;
                            readElfSymbols(base, symbols);
                        }
                        for (int i = 0; i < elf.e_shnum; i++) {
                            ElfSection section = elf.getSection(i);
                            ElfSectionHeader header = section.header;
                            if (".eh_frame".equals(header.getName())) {
                                ByteBuffer buffer = ByteBuffer.wrap(section.getData()).order(ExceptionFrameParser.byteOrder(elf));
                                List<ExceptionFrameParser.FunctionRange> ranges = ExceptionFrameParser.extractFunctionRanges(buffer, section.header.sh_addr, elf.ei_class == ElfFile.CLASS_64 ? 8 : 4);
                                for (ExceptionFrameParser.FunctionRange range : ranges) {
                                    FunctionInfo fi = new FunctionInfo(null, base + range.start, base + range.end);
                                    all_functions.put(base + range.start, fi);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        }
    }

    private static FunctionInfo cacheWindowsExport(String name) {
        long start = SymbolLookup.lookup(name);
        if (start == 0) {
            return null;
        }

        FunctionInfo info = all_functions.get(start);
        if (info == null) {
            info = new FunctionInfo(name, start, start);
        } else if (info.name == null) {
            info = new FunctionInfo(name, info.start, info.end);
        }
        all_functions.put(start, info);
        named_functions.put(name, info);
        return info;
    }

    private static void readElfSymbols(long base, ElfSymbol[] symbols) {
        if (symbols != null) {
            for (ElfSymbol symbol : symbols) {
                if (symbol.getType() == 2) {
                    long start = base + symbol.st_value;
                    long end = base + symbol.st_value + symbol.st_size;
                    String name = symbol.getName();
                    FunctionInfo function = new FunctionInfo(name, start, end);
                    all_functions.put(start, function);
                    named_functions.put(name, function);
                }
            }
        }
    }

    private static final class ExceptionFrameParser {
        private static final int DW_EH_PE_OMIT = 0xff;
        private static final int DW_EH_PE_ABSPTR = 0x00;
        private static final int DW_EH_PE_ULEB128 = 0x01;
        private static final int DW_EH_PE_UDATA2 = 0x02;
        private static final int DW_EH_PE_UDATA4 = 0x03;
        private static final int DW_EH_PE_UDATA8 = 0x04;
        private static final int DW_EH_PE_SLEB128 = 0x09;
        private static final int DW_EH_PE_SDATA2 = 0x0a;
        private static final int DW_EH_PE_SDATA4 = 0x0b;
        private static final int DW_EH_PE_SDATA8 = 0x0c;
        private static final int DW_EH_PE_PCREL = 0x10;
        private static final int DW_EH_PE_INDIRECT = 0x80;

        private static List<FunctionRange> extractFunctionRanges(byte[] ehFrame, long ehFrameAddress, ByteOrder order, int addressSize) {
            return extractFunctionRanges(ByteBuffer.wrap(ehFrame).order(order), ehFrameAddress, addressSize);
        }

        private static List<FunctionRange> extractFunctionRanges(ByteBuffer ehFrame, long ehFrameAddress, int addressSize) {
            ByteBuffer buffer = ehFrame.duplicate().order(ehFrame.order());
            List<FunctionRange> ranges = new ArrayList<>();
            Map<Long, CieInfo> cies = new HashMap<>();

            while (buffer.hasRemaining()) {
                int entryOffset = buffer.position();
                EntryHeader header = readEntryHeader(buffer);
                if (header == null) {
                    break;
                }

                int contentEnd = checkedEntryEnd(buffer, entryOffset, header);
                if (contentEnd < 0) {
                    break;
                }

                int ciePointerOffset = buffer.position();
                long ciePointer = readUnsigned(buffer, header.idSize);
                if (ciePointer == 0) {
                    cies.put((long) entryOffset, readCie(buffer, entryOffset, contentEnd, addressSize));
                } else {
                    CieInfo cie = cies.get((long) ciePointerOffset - ciePointer);
                    FunctionRange range = cie == null ? null : readFde(buffer, ehFrameAddress, contentEnd, addressSize, cie);
                    if (range != null) {
                        ranges.add(range);
                    }
                }

                buffer.position(contentEnd);
            }

            return ranges;
        }

        private static CieInfo readCie(ByteBuffer buffer, int entryOffset, int contentEnd, int addressSize) {
            int version = readUnsignedByte(buffer);
            String augmentation = readCString(buffer, contentEnd);
            readUleb128(buffer);
            readSleb128(buffer);
            if (version == 1) {
                readUnsignedByte(buffer);
            } else {
                readUleb128(buffer);
            }

            int fdeEncoding = DW_EH_PE_ABSPTR;
            if (augmentation.startsWith("z") && buffer.position() < contentEnd) {
                long augmentationLength = readUleb128(buffer);
                int augmentationEnd = (int) Math.min((long) buffer.position() + augmentationLength, contentEnd);

                for (int i = 1; i < augmentation.length() && buffer.position() < augmentationEnd; i++) {
                    char ch = augmentation.charAt(i);
                    if (ch == 'L') {
                        readUnsignedByte(buffer);
                    } else if (ch == 'P') {
                        int encoding = readUnsignedByte(buffer);
                        skipEncodedPointer(buffer, addressSize, encoding);
                    } else if (ch == 'R') {
                        fdeEncoding = readUnsignedByte(buffer);
                    }
                }

                buffer.position(augmentationEnd);
            }

            return new CieInfo(entryOffset, fdeEncoding);
        }

        private static FunctionRange readFde(ByteBuffer buffer, long ehFrameAddress, int contentEnd, int addressSize, CieInfo cie) {
            if (cie.fdeEncoding == DW_EH_PE_OMIT || !canResolve(cie.fdeEncoding)) {
                return null;
            }

            long start = readEncodedPointer(buffer, ehFrameAddress, addressSize, cie.fdeEncoding, true);
            long size = readEncodedPointer(buffer, ehFrameAddress, addressSize, cie.fdeEncoding, false);
            if (size <= 0 || buffer.position() > contentEnd) {
                return null;
            }
            return new FunctionRange(start, start + size, cie.offset);
        }

        private static boolean canResolve(int encoding) {
            if ((encoding & DW_EH_PE_INDIRECT) != 0) {
                return false;
            }
            int application = encoding & 0x70;
            return application == DW_EH_PE_ABSPTR || application == DW_EH_PE_PCREL;
        }

        private static long readEncodedPointer(ByteBuffer buffer, long sectionAddress, int addressSize, int encoding, boolean applyRelative) {
            int position = buffer.position();
            long value = readEncodedValue(buffer, addressSize, encoding & 0x0f);
            int application = encoding & 0x70;

            if (applyRelative && application == DW_EH_PE_PCREL) {
                value += sectionAddress + position;
            } else if (applyRelative && application != DW_EH_PE_ABSPTR) {
                throw new IllegalArgumentException("Unsupported pointer encoding: 0x" + Integer.toHexString(encoding));
            }

            if ((encoding & DW_EH_PE_INDIRECT) != 0) {
                throw new IllegalArgumentException("Indirect pointer encoding needs process memory: 0x" + Integer.toHexString(encoding));
            }

            return value;
        }

        private static void skipEncodedPointer(ByteBuffer buffer, int addressSize, int encoding) {
            if (encoding != DW_EH_PE_OMIT) {
                readEncodedValue(buffer, addressSize, encoding & 0x0f);
            }
        }

        private static long readEncodedValue(ByteBuffer buffer, int addressSize, int format) {
            switch (format) {
                case DW_EH_PE_ABSPTR:
                    return readUnsigned(buffer, addressSize);
                case DW_EH_PE_ULEB128:
                    return readUleb128(buffer);
                case DW_EH_PE_UDATA2:
                    return readUnsigned(buffer, 2);
                case DW_EH_PE_UDATA4:
                    return readUnsigned(buffer, 4);
                case DW_EH_PE_UDATA8:
                case DW_EH_PE_SDATA8:
                    return buffer.getLong();
                case DW_EH_PE_SLEB128:
                    return readSleb128(buffer);
                case DW_EH_PE_SDATA2:
                    return buffer.getShort();
                case DW_EH_PE_SDATA4:
                    return buffer.getInt();
                default:
                    throw new IllegalArgumentException("Unsupported pointer format: 0x" + Integer.toHexString(format));
            }
        }

        private static EntryHeader readEntryHeader(ByteBuffer buffer) {
            if (buffer.remaining() < 4) {
                return null;
            }

            long length = readUnsigned(buffer, 4);
            if (length == 0) {
                return null;
            }
            if (length == 0xffffffffL) {
                if (buffer.remaining() < 8) {
                    return null;
                }
                return new EntryHeader(buffer.getLong(), 12, 8);
            }
            return new EntryHeader(length, 4, 4);
        }

        private static int checkedEntryEnd(ByteBuffer buffer, int entryOffset, EntryHeader header) {
            long contentEnd = (long) entryOffset + header.lengthFieldSize + header.length;
            if (contentEnd < buffer.position() || contentEnd > buffer.limit() || contentEnd > Integer.MAX_VALUE) {
                return -1;
            }
            return (int) contentEnd;
        }

        private static long readUnsigned(ByteBuffer buffer, int bytes) {
            switch (bytes) {
                case 1:
                    return readUnsignedByte(buffer);
                case 2:
                    return buffer.getShort() & 0xffffL;
                case 4:
                    return buffer.getInt() & 0xffffffffL;
                case 8:
                    return buffer.getLong();
                default:
                    throw new IllegalArgumentException("Unsupported unsigned width: " + bytes);
            }
        }

        private static int readUnsignedByte(ByteBuffer buffer) {
            return buffer.get() & 0xff;
        }

        private static long readUleb128(ByteBuffer buffer) {
            long result = 0;
            int shift = 0;
            while (buffer.hasRemaining()) {
                int b = readUnsignedByte(buffer);
                result |= (long) (b & 0x7f) << shift;
                if ((b & 0x80) == 0) {
                    break;
                }
                shift += 7;
            }
            return result;
        }

        private static long readSleb128(ByteBuffer buffer) {
            long result = 0;
            int shift = 0;
            int b;
            do {
                b = readUnsignedByte(buffer);
                result |= (long) (b & 0x7f) << shift;
                shift += 7;
            } while ((b & 0x80) != 0 && buffer.hasRemaining());

            if (shift < Long.SIZE && (b & 0x40) != 0) {
                result |= -1L << shift;
            }
            return result;
        }

        private static String readCString(ByteBuffer buffer, int limit) {
            StringBuilder builder = new StringBuilder();
            while (buffer.position() < limit) {
                int b = readUnsignedByte(buffer);
                if (b == 0) {
                    break;
                }
                builder.append((char) b);
            }
            return builder.toString();
        }

        private static ByteOrder byteOrder(ElfFile elf) {
            return elf.ei_data == ElfFile.DATA_MSB ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        }

        private static final class FunctionRange {
            public final long start;
            public final long end;
            public final long cieOffset;

            private FunctionRange(long start, long end, long cieOffset) {
                this.start = start;
                this.end = end;
                this.cieOffset = cieOffset;
            }

            @Override
            public String toString() {
                return "0x" + Long.toHexString(start) + "..0x" + Long.toHexString(end) + " cie=0x" + Long.toHexString(cieOffset);
            }
        }

        private static final class CieInfo {
            private final long offset;
            private final int fdeEncoding;

            private CieInfo(long offset, int fdeEncoding) {
                this.offset = offset;
                this.fdeEncoding = fdeEncoding;
            }
        }

        private static final class EntryHeader {
            private final long length;
            private final int lengthFieldSize;
            private final int idSize;

            private EntryHeader(long length, int lengthFieldSize, int idSize) {
                this.length = length;
                this.lengthFieldSize = lengthFieldSize;
                this.idSize = idSize;
            }
        }
    }
}
