package alice.injector;

import alice.Platform;
import alice.log.Logger;
import alice.util.FileUtil;
import alice.util.ProcReader;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import sun.jvm.hotspot.debugger.win32.coff.COFFException;
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
public final class SymbolLookup {
    private static final Object2LongOpenHashMap<String> bases = new Object2LongOpenHashMap<>();
    private static final Object2LongOpenHashMap<String> cache = new Object2LongOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, ExportDirectoryTable> exports = new Object2ObjectOpenHashMap<>();

    private static ExportDirectoryTable getExport(String lib) {
        ExportDirectoryTable table;
        if (!exports.containsKey(lib)) {
            Logger.MAIN.info("Parsing library: ".concat(lib).concat("."));
            try {
                table = COFFFileParser.getParser().parse(lib).getHeader().getOptionalHeader().getDataDirectories().getExportDirectoryTable();
            } catch (COFFException e) {
                Logger.MAIN.error("Failed to parse library: ".concat(lib).concat("."));
                return null;
            }
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
     * Look up a symbol from all loaded libraries.
     *
     * @param symbol the symbol to lookup.
     * @return the symbol value in the current process.
     */
    public static long lookup(String symbol) {
        if (cache.containsKey(symbol)) {
            return cache.getLong(symbol);
        }
        final long[] ret = {0};
        bases.keySet().forEach(lib -> {
            long base = bases.getLong(lib);
            if (!Platform.win32) {
                if (isElf(lib)) {
                    Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
                    if (symbols.containsKey(symbol)) {
                        ret[0] = base + symbols.get(symbol).offset;
                    }
                }
            } else {
                ExportDirectoryTable table = getExport(lib);
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
        });

        if (ret[0] != 0) {
            cache.put(symbol, ret[0]);
            return ret[0];
        }

        ProcReader.parseProcMaps().forEach((path, mappings) -> {
            if (FileUtil.exists(path) && !FileUtil.isDirectory(path)) {
                if (!bases.containsKey(path)) {//We already checked the cache,so skip things exist in the cache.
                    long base = Long.MAX_VALUE;

                    for (ProcReader.MemoryMapping mapping : mappings) {
                        base = Math.min(Long.parseLong(Platform.win32 ? mapping.addressRangeStart.substring(2) : mapping.addressRangeStart, 16), base);
                    }

                    if (base != Long.MAX_VALUE) {
                        bases.put(path, base);
                        if (!Platform.win32) {
                            if (isElf(path)) {

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
                        Logger.MAIN.error("Failed to get base of " + path + '.');
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
     *
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
     * Look up a symbol from a specific dynamic library.<br>
     * If param lib is null, it will search in all libraries.
     *
     * @param lib    the dynamic library to use
     * @param symbol the symbol to lookup
     * @return the symbol value in the current process
     */
    public static long lookup(String lib, String symbol) {
        if (lib == null) {
            return lookup(symbol);
        }
        if (!Paths.get(lib).isAbsolute()) {
            lib = toAbsoluteLibPath(lib);
        }
        if (cache.containsKey(symbol)) {
            return cache.getLong(symbol);
        }
        if (!Platform.win32 && !isElf(lib)) {
            System.err.println("Not an elf file:" + lib);
            return 0;
        }
        long base = lookupLibBase(lib);
        if (base == 0) {
            return 0;
        }
        if (!Platform.win32) {
            Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
            if (symbols.containsKey(symbol)) {
                return symbols.get(symbol).offset + base;
            }
        } else {
            ExportDirectoryTable table = getExport(lib);
            long ret = 0;
            for (int i = 0; i < Objects.requireNonNull(table).getNumberOfNamePointers(); i++) {
                short ordinal = table.getExportOrdinal(i);
                String name = table.getExportName(i);
                long address = table.getExportAddress(ordinal);
                if (name.equals(symbol)) {
                    ret = base + address;
                }
                cache.put(name, base + address);
            }
            return ret;
        }
        return 0;
    }

    public static long lookupLibBase(String lib) {
        if (lib == null) {
            return 0;
        }
        if (!Paths.get(lib).isAbsolute()) {
            lib = toAbsoluteLibPath(lib);
        }
        long _try = bases.getLong(lib);
        if (_try != 0) {
            return _try;
        }
        _try = Long.MAX_VALUE;
        Map<String, LinkedList<ProcReader.MemoryMapping>> maps = parseProcMaps();
        LinkedList<ProcReader.MemoryMapping> mappings = maps.get(lib);
        for (ProcReader.MemoryMapping mapping : mappings) {
            _try = Math.min(Long.parseLong(mapping.addressRangeStart, 16), _try);
        }
        if (_try != Long.MAX_VALUE) {
            bases.put(lib, _try);
            return _try;
        }
        Logger.MAIN.error("Cannot find base of ".concat(lib).concat("!"));
        return 0;
    }
}
