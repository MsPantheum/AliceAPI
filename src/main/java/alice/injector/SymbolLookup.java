package alice.injector;

import alice.util.FileUtil;
import alice.util.ProcReader;
import alice.util.ProcessUtil;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.Map;

public class SymbolLookup {
    private static final Object2LongOpenHashMap<String> bases  = new Object2LongOpenHashMap<>();
    private static final Object2LongOpenHashMap<String> cache  = new Object2LongOpenHashMap<>();


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

    public static long lookup(String lib, String symbol) {
        if(cache.containsKey(symbol)) {
            return  cache.getLong(symbol);
        }
        final long[] base = {0};
        if(bases.containsKey(lib)) {
            base[0] = bases.getLong(lib);
        } else {
            ProcReader.parseProcMaps(ProcessUtil.getPID()).forEach(mm -> {
                if(mm.pathname.equals(lib)){
                    base[0] = Long.parseLong(mm.addressRangeStart,16);
                    bases.put(lib, base[0]);

                }
            });
        }
        if(base[0] == 0){
            return 0;
        }
        Map<String, ProcReader.SymbolInfo> symbols = ProcReader.readElf(lib);
        if(symbols.containsKey(symbol)){
            return symbols.get(symbol).offset + base[0];
        }
        return 0;
    }
}
