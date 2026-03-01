package alice.injector;

import alice.Platform;
import alice.util.FileUtil;
import alice.util.ProcReader;
import alice.util.ProcessUtil;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import sun.jvm.hotspot.debugger.win32.coff.COFFFileParser;
import sun.jvm.hotspot.debugger.win32.coff.ExportDirectoryTable;

import java.io.File;
import java.util.Map;

public class SymbolLookup {
    private static final Object2LongOpenHashMap<String> bases  = new Object2LongOpenHashMap<>();
    private static final Object2LongOpenHashMap<String> cache  = new Object2LongOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String,ExportDirectoryTable> exports = new Object2ObjectOpenHashMap<>();

    public static long lookup(String symbol) {
        if(cache.containsKey(symbol)) {
            return cache.getLong(symbol);
        }
        final long[] ret = {0};
        bases.keySet().forEach(lib -> {
            long base = bases.getLong(symbol);
            Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
            if(symbols.containsKey(symbol)){
                ret[0] = base + symbols.get(symbol).offset;
            }
        });

        ProcReader.parseProcMaps(ProcessUtil.getPID()).forEach(mm -> {
            if(FileUtil.exists(mm.pathname)){
                if (!bases.containsKey(mm.pathname)) {
                    long base = Long.parseLong(mm.addressRangeStart, 16);
                    bases.put(mm.pathname, base);
                    Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(mm.pathname);
                    if (symbols.containsKey(symbol)) {
                        ret[0] = base + symbols.get(symbol).offset;
                    }
                }
            }
        });
        if(ret[0] != 0){
            cache.put(symbol, ret[0]);
        }
        return ret[0];
    }

    private static String toAbsoluteLibPath(String lib) {
        lib = File.separator + lib;
        for (String p : bases.keySet()) {
            if(p.endsWith(lib)) {
                return p;
            }
        }
        for (ProcReader.MemoryMapping mapping : ProcReader.parseProcMaps()) {
            if(mapping.pathname.endsWith(lib)){
                return mapping.pathname;
            }
        }
        throw new RuntimeException("Can't find absolute lib path for " + lib);
    }

    public static long lookup(String lib, String symbol) {
        if(!lib.startsWith(File.separator)){
            lib = toAbsoluteLibPath(lib);
        }
        if(cache.containsKey(symbol)) {
            return  cache.getLong(symbol);
        }
        final long[] base = {0};
        if(bases.containsKey(lib)) {
            base[0] = bases.getLong(lib);
        } else {
            if(!Platform.win32) {
                for (ProcReader.MemoryMapping mapping : ProcReader.parseProcMaps()) {
                    if (mapping.pathname.equals(lib)) {
                        base[0] = Long.parseLong(mapping.addressRangeStart, 16);
                        bases.put(lib, base[0]);
                        break;
                    }
                }
            } else {
                NativeLibrary Nlib = NativeLibrary.load(lib,false);
                if(Nlib == null){
                    throw new RuntimeException("Can't load Native library for " + lib);
                }
                base[0] = Nlib.getBase();
                bases.put(lib, base[0]);
            }
        }
        if(base[0] == 0){
            System.err.println("Cannot find base of " + lib + "!");
            return 0;
        }
        if(!Platform.win32){
            Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
            if (symbols.containsKey(symbol)) {
                return symbols.get(symbol).offset + base[0];
            }
        } else {
            ExportDirectoryTable table;
            if(!exports.containsKey(lib)){
                table = COFFFileParser.getParser().parse(lib).getHeader().getOptionalHeader().getDataDirectories().getExportDirectoryTable();
                exports.put(lib, table);
            } else {
                table = exports.get(lib);
            }
            long ret = 0;
            for (int i = 0; i < table.getNumberOfNamePointers(); i++) {
                short ordinal = table.getExportOrdinal(i);
                String name = table.getExportName(i);
                long address = table.getExportAddress(ordinal);
                if(name.equals(symbol)){
                    ret = base[0] + address;
                }
                cache.put(name, base[0] + address);
            }
            return ret;
        }
        return 0;
    }
}
