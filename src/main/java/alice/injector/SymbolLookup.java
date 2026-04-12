package alice.injector;

import alice.Platform;
import alice.util.FileUtil;
import alice.util.ProcReader;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import sun.jvm.hotspot.debugger.win32.coff.COFFFileParser;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static alice.util.ProcReader.isElf;
import static alice.util.ProcReader.parseProcMaps;

/**
 * Lookup symbols in dynamic libraries.
 */
public class SymbolLookup {
    private static final Object2LongOpenHashMap<String> bases = new Object2LongOpenHashMap<>();
    private static final Object2LongOpenHashMap<String> cache = new Object2LongOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, ExportDirectoryTable> exports = new Object2ObjectOpenHashMap<>();

    private static ExportDirectoryTable getExport(String lib) {
        ExportDirectoryTable table;
        if (!exports.containsKey(lib)) {
            table = COFFFileParser.getParser().parse(lib).getHeader().getOptionalHeader().getDataDirectories().getExportDirectoryTable();
            if (table == null) {
                return null;
            }
            exports.put(lib, table);
        } else {
            table = exports.get(lib);
        }
        return table;
    }

    /**
     * Lookup up a symbol from all loaded libraries.
     *
     * @param symbol the symbol to lookup.
     * @return the symbol value in current process.
     */
    public static long lookup(String symbol) {
        if (cache.containsKey(symbol)) {
            return cache.getLong(symbol);
        }
        final long[] ret = {0};
        bases.keySet().forEach(lib -> {
            long base = bases.getLong(lib);
            if (!Platform.win32) {
                if(isElf(lib)){
                    Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
                    if (symbols.containsKey(symbol)) {
                        ret[0] = base + symbols.get(symbol).offset;
                    }
                }
            } else {
                ExportDirectoryTable table = getExport(lib);
                for (int i = 0; i < Objects.requireNonNull(table).getNumberOfNamePointers(); i++) {
                    short ordinal = table.getExportOrdinal(i);
                    String name = table.getExportName(i);
                    long address = table.getExportAddress(ordinal);
                    if (name.equals(symbol)) {
                        ret[0] = base + address;
                    }
                    cache.put(name, base + address);
                }
            }
        });

        if (ret[0] != 0) {
            cache.put(symbol, ret[0]);
            return ret[0];
        }

        ProcReader.parseProcMaps().forEach((path,mappings) -> {
            if (FileUtil.exists(path) && !FileUtil.isDirectory(path)) {
                if (!bases.containsKey(path)) {//We already checked the cache,so skip things exist in the cache.
                    long base = Long.MAX_VALUE;

                    for (ProcReader.MemoryMapping mapping : mappings) {
                        base = Math.min(Long.parseLong(Platform.win32 ? mapping.addressRangeStart.substring(2) : mapping.addressRangeStart, 16),base);
                    }

                    if(base != Long.MAX_VALUE) {
                        bases.put(path, base);
                        if (!Platform.win32) {
                            if(isElf(path)){

                                Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(path);
                                if (symbols.containsKey(symbol)) {
                                    ret[0] = base + symbols.get(symbol).offset;
                                }

                            }
                        } else {
                            ExportDirectoryTable table = getExport(path);
                            if (table != null) {
                                for (int i = 0; i < table.getNumberOfNamePointers(); i++) {
                                    short ordinal = table.getExportOrdinal(i);
                                    String name = table.getExportName(i);
                                    long address = table.getExportAddress(ordinal);
                                    if (name.equals(symbol)) {
                                        ret[0] = base + address;

                                    }
                                    cache.put(name, base + address);
                                }
                            }
                        }
                    } else {
                        System.err.println("Error:Failed to get base of " + path);
                    }
                }
            }
        });
        if (ret[0] != 0) {
            cache.put(symbol, ret[0]);
        }
        return ret[0];
    }

    /**
     * Convert the path of a dynamic library to an absolute path.
     * @param lib the path of the dynamic library.
     * @return the absolute path of the dynamic library.
     */
    public static String toAbsoluteLibPath(String lib) {
        if (!Platform.win32) {
            lib = File.separator + lib;
        }
        for (String p : bases.keySet()) {
            if (p.endsWith(lib)) {
                return p;
            }
        }
        for (String path : ProcReader.parseProcMaps().keySet()) {
            if (path.endsWith(lib)) {
                return path;
            }
        }
        throw new RuntimeException("Can't find absolute lib path for " + lib);
    }

    /**
     * Lookup up a symbol from a specific dynamic library.
     * @param lib the dynamic library to use.
     * @param symbol the symbol to lookup.
     * @return the symbol value in current process.
     */
    public static long lookup(String lib, String symbol) {
        if (!Paths.get(lib).isAbsolute()) {
            lib = toAbsoluteLibPath(lib);
        }
        if (cache.containsKey(symbol)) {
            return cache.getLong(symbol);
        }
        if(!Platform.win32 && !isElf(lib)){
            System.err.println("Not an elf file:"+lib);
            return 0;
        }
        final long[] base = {Long.MAX_VALUE};
        if (bases.containsKey(lib)) {
            base[0] = bases.getLong(lib);
        } else {
            if (!Platform.win32) {
                Map<String, LinkedList<ProcReader.MemoryMapping>> maps = parseProcMaps();
                LinkedList<ProcReader.MemoryMapping> mappings = maps.get(lib);
                for (ProcReader.MemoryMapping mapping : mappings) {
                    base[0] = Math.min(Long.parseLong(mapping.addressRangeStart, 16),base[0]);
                }
                bases.put(lib, base[0]);
            } else {
                NativeLibrary Nlib = NativeLibrary.load(lib, false);
                if (Nlib == null) {
                    throw new RuntimeException("Can't load Native library for " + lib);
                }
                base[0] = Nlib.getBase();
                bases.put(lib, base[0]);
            }
        }
        if (base[0] == Long.MAX_VALUE) {
            System.err.println("Cannot find base of " + lib + "!");
            return 0;
        }
        if (!Platform.win32) {
            Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
            if (symbols.containsKey(symbol)) {
                return symbols.get(symbol).offset + base[0];
            }
        } else {
            ExportDirectoryTable table = getExport(lib);
            long ret = 0;
            for (int i = 0; i < Objects.requireNonNull(table).getNumberOfNamePointers(); i++) {
                short ordinal = table.getExportOrdinal(i);
                String name = table.getExportName(i);
                long address = table.getExportAddress(ordinal);
                if (name.equals(symbol)) {
                    ret = base[0] + address;
                }
                cache.put(name, base[0] + address);
            }
            return ret;
        }
        return 0;
    }
}
