package alice.injector;

import alice.Init;
import alice.Platform;
import alice.log.Logger;
import alice.util.FileUtil;
import alice.util.ProcReader;
import alice.util.SymbolInfo;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.lang3.Strings;
import sun.jvm.hotspot.debugger.win32.coff.COFFFileParser;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static alice.util.ProcReader.isElf;
import static alice.util.ProcReader.parseProcMaps;

/**
 * Lookup symbols in dynamic libraries.
 */
public final class SymbolLookup {
    private static final Object2LongOpenHashMap<String> bases = new Object2LongOpenHashMap<>();
    private static final Map<String, Map<String, SymbolInfo>> cache = new HashMap<>();
    private static final Long2ObjectMap<String> reverseCache = new Long2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, ExportDirectoryTable> exports = new Object2ObjectOpenHashMap<>();

    private static SymbolInfo cacheSymbol(String lib, String name, long address) {
        SymbolInfo ret = new SymbolInfo(lib, name, address);
        cache.computeIfAbsent(lib, key -> new HashMap<>()).put(name, ret);
        reverseCache.put(address, name);
        return ret;
    }

    private static SymbolInfo lookupInCache(String lib, String name) {
        if (lib != null) {
            return cache.computeIfAbsent(lib, key -> new HashMap<>()).get(name);
        } else {
            for (Map<String, SymbolInfo> value : cache.values()) {
                SymbolInfo ret = value.get(name);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    private static ExportDirectoryTable getExport(String lib) {
        ExportDirectoryTable table;
        if (!exports.containsKey(lib)) {
            Logger.MAIN.info("Parsing library: ".concat(lib).concat("."));
            try {
                table = COFFFileParser.getParser().parse(lib).getHeader().getOptionalHeader().getDataDirectories().getExportDirectoryTable();
            } catch (Exception e) {
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
     * Look up the value of a symbol from all loaded libraries.
     *
     * @param symbol the symbol to lookup.
     * @return the symbol value in the current process,0 if not found
     */
    public static long lookupValue(String symbol) {
        SymbolInfo info = lookup(symbol);
        return info != null ? info.address : 0;
    }

    /**
     * Look up a symbol from all loaded libraries.
     *
     * @param symbol the symbol to lookup.
     * @return the symbol info in the current process.
     */
    public static SymbolInfo lookup(String symbol) {
        SymbolInfo _try = lookupInCache(null, symbol);
        if (_try != null) {
            return _try;
        }
        final SymbolInfo[] ret = {null};
        bases.keySet().forEach(lib -> {
            long base = bases.getLong(lib);
            if (!Platform.win32) {
                if (isElf(lib)) {
                    Map<String, ProcReader.ElfSymbolInfo> symbols = ProcReader.readElf(lib);
                    if (symbols.containsKey(symbol)) {
                        ret[0] = cacheSymbol(lib, symbol, base + symbols.get(symbol).offset);
                    }
                }
            } else {
                ExportDirectoryTable table = getExport(lib);
                if (table != null) {
                    for (int i = 0; i < table.getNumberOfNamePointers(); i++) {
                        short ordinal = table.getExportOrdinal(i);
                        String name = table.getExportName(i);
                        long address = table.getExportAddress(ordinal);
                        SymbolInfo current = cacheSymbol(lib, name, base + address);
                        if (name.equals(symbol)) {
                            ret[0] = current;
                        }

                    }
                }
            }
        });

        if (ret[0] != null) {
            //cacheSymbol(symbol, ret[0]);
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
                                Map<String, ProcReader.ElfSymbolInfo> symbols = ProcReader.readElf(path);
                                if (symbols.containsKey(symbol)) {
                                    ret[0] = cacheSymbol(path, symbol, base + symbols.get(symbol).offset);
                                }
                            }
                        } else {
                            ExportDirectoryTable table = getExport(path);
                            if (table != null) {
                                for (int i = 0; i < table.getNumberOfNamePointers(); i++) {
                                    short ordinal = table.getExportOrdinal(i);
                                    String name = table.getExportName(i);
                                    long address = table.getExportAddress(ordinal);
                                    SymbolInfo current = cacheSymbol(path, name, base + address);
                                    if (name.equals(symbol)) {
                                        ret[0] = current;
                                    }
                                }
                            }
                        }
                    } else {
                        Logger.MAIN.error("Failed to get base of " + path + '.');
                    }
                }
            }
        });
        return ret[0];
    }

    /**
     * Look up a symbol name by its address from all loaded libraries.
     *
     * @param address the symbol address in the current process
     * @return the symbol name, or null if not found
     */
    public static String lookup(long address) {
        if (reverseCache.containsKey(address)) {
            return reverseCache.get(address);
        }
        final String[] ret = {null};
        bases.keySet().forEach(lib -> {
            if (ret[0] == null) {
                ret[0] = lookup(lib, address);
            }
        });
        if (ret[0] != null) {
            return ret[0];
        }
        ProcReader.parseProcMaps().forEach((path, mappings) -> {
            if (ret[0] == null && FileUtil.exists(path) && !FileUtil.isDirectory(path)) {
                if (!bases.containsKey(path)) {
                    long base = Long.MAX_VALUE;

                    for (ProcReader.MemoryMapping mapping : mappings) {
                        base = Math.min(Long.parseLong(Platform.win32 ? mapping.addressRangeStart.substring(2) : mapping.addressRangeStart, 16), base);
                    }

                    if (base != Long.MAX_VALUE) {
                        bases.put(path, base);
                    } else {
                        Logger.MAIN.error("Failed to get base of " + path + '.');
                        return;
                    }
                }
                ret[0] = lookup(path, address);
            }
        });
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
            if (Platform.win32 ? Strings.CI.endsWith(p, lib) : p.endsWith(lib)) {//Windows is case-insensitive. Sh*t.
                return p;
            }
        }
        for (String path : ProcReader.parseProcMaps().keySet()) {
            if (Platform.win32 ? Strings.CI.endsWith(path, lib) : path.endsWith(lib)) {
                return path;
            }
        }
        throw new RuntimeException("Can't find absolute lib path for " + lib);
    }

    /**
     * Look up the value of a symbol from a specific dynamic library.<br>
     * If param lib is null, it will search in all libraries.
     *
     * @param lib    the dynamic library to use
     * @param symbol the symbol to lookup
     * @return the symbol value in the current process,0 if not found.
     */
    public static long lookupValue(String lib, String symbol) {
        SymbolInfo info = lookup(lib, symbol);
        return info != null ? info.address : 0;
    }

    /**
     * Look up a symbol from a specific dynamic library.<br>
     * If param lib is null, it will search in all libraries.
     *
     * @param lib    the dynamic library to use
     * @param symbol the symbol to lookup
     * @return the symbol info in the current process
     */
    public static SymbolInfo lookup(String lib, String symbol) {
        if (lib == null) {
            return lookup(symbol);
        }
        if (!Paths.get(lib).isAbsolute()) {
            lib = toAbsoluteLibPath(lib);
        }
        SymbolInfo _try = lookupInCache(lib, symbol);
        if (_try != null) {
            return _try;
        }
        if (!Platform.win32 && !isElf(lib)) {
            System.err.println("Not an elf file:" + lib);
            return null;
        }
        long base = lookupLibBase(lib);
        if (base == 0) {
            return null;
        }
        if (!Platform.win32) {
            Map<String, ProcReader.ElfSymbolInfo> symbols = ProcReader.readElf(lib);
            if (symbols.containsKey(symbol)) {
                long ret = symbols.get(symbol).offset + base;
                return cacheSymbol(lib, symbol, ret);
            }
        } else {
            ExportDirectoryTable table = getExport(lib);
            SymbolInfo ret = null;
            for (int i = 0; i < Objects.requireNonNull(table).getNumberOfNamePointers(); i++) {
                short ordinal = table.getExportOrdinal(i);
                String name = table.getExportName(i);
                long address = table.getExportAddress(ordinal);
                SymbolInfo current = cacheSymbol(lib, name, base + address);
                if (name.equals(symbol)) {
                    ret = current;
                }

            }
            return ret;
        }
        return null;
    }

    /**
     * Look up a symbol name by its address from a specific dynamic library.<br>
     * If param lib is null, it will search in all libraries.
     *
     * @param lib     the dynamic library to use
     * @param address the symbol address in the current process
     * @return the symbol name, or null if not found
     */
    public static String lookup(String lib, long address) {
        if (lib == null) {
            return lookup(address);
        }
        if (!Paths.get(lib).isAbsolute()) {
            lib = toAbsoluteLibPath(lib);
        }
        if (!Platform.win32 && !isElf(lib)) {
            System.err.println("Not an elf file:" + lib);
            return null;
        }
        long base = lookupLibBase(lib);
        if (base == 0) {
            return null;
        }
        if (!Platform.win32) {
            Map<String, ProcReader.ElfSymbolInfo> symbols = ProcReader.readElf(lib);
            for (Map.Entry<String, ProcReader.ElfSymbolInfo> entry : symbols.entrySet()) {
                long symbolAddress = entry.getValue().offset + base;
                cacheSymbol(lib, entry.getKey(), symbolAddress);
                if (symbolAddress == address) {
                    return entry.getKey();
                }
            }
        } else {
            ExportDirectoryTable table = getExport(lib);
            if (table == null) {
                return null;
            }
            for (int i = 0; i < table.getNumberOfNamePointers(); i++) {
                short ordinal = table.getExportOrdinal(i);
                String name = table.getExportName(i);
                long symbolAddress = base + table.getExportAddress(ordinal);
                cacheSymbol(lib, name, symbolAddress);
                if (symbolAddress == address) {
                    return name;
                }
            }
        }
        return null;
    }

    /**
     * Rebuild the cache of loaded library base addresses and return them.
     *
     * @return an unmodifiable snapshot of bases of all the resolved dynamic libraries.
     */
    public static Map<String, Long> getAllBases() {
        Object2LongOpenHashMap<String> rebuiltBases = new Object2LongOpenHashMap<>();
        parseProcMaps().forEach((lib, mappings) -> {
            long base = getBase(mappings);
            if (base != 0) {
                rebuiltBases.put(lib, base);
            }
        });

        bases.clear();
        bases.putAll(rebuiltBases);
        return Collections.unmodifiableMap(new HashMap<>(bases));
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
        Map<String, ArrayList<ProcReader.MemoryMapping>> maps = parseProcMaps();
        ArrayList<ProcReader.MemoryMapping> mappings = maps.get(lib);
        _try = getBase(mappings);
        if (_try != 0) {
            bases.put(lib, _try);
            return _try;
        }
        Logger.MAIN.error("Cannot find base of ".concat(lib).concat("!"));
        return 0;
    }

    public static long getBase(ArrayList<ProcReader.MemoryMapping> mappings) {
        long base = Long.MAX_VALUE;
        for (ProcReader.MemoryMapping mapping : mappings) {
            base = Math.min(Long.parseLong(mapping.addressRangeStart, 16), base);
        }
        return base == Long.MAX_VALUE ? 0 : base;
    }

    public static void main(String[] args) {
        if (Platform.win32) {
            Init.ensureInit();
        }
        switch (args.length) {
            case 0: {
                System.err.println("Usage: SymbolLookup <lib> <symbol>");
                return;
            }
            case 1: {
                String symbol = args[0];
                System.out.println("0x".concat(Long.toHexString(lookup(symbol).address)));
                return;
            }
            default: {
                if (args.length > 2) {
                    System.out.println("Warning: arguments ".concat(Arrays.toString(Arrays.copyOfRange(args, 2, args.length))).concat(" will be ignored"));
                }
                String lib = args[0];
                if (!lib.contains(".")) {
                    lib = System.mapLibraryName(lib);
                }
                String symbol = args[1];
                System.out.println("0x".concat(Long.toHexString(Objects.requireNonNull(lookup(lib, symbol)).address)));
            }
        }
    }
}
