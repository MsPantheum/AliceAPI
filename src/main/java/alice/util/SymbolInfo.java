package alice.util;

public class SymbolInfo {
    public final String library;
    public final String name;
    public final long address;

    public SymbolInfo(String library, String name, long address) {
        this.library = library;
        this.name = name;
        this.address = address;
    }

    @Override
    public int hashCode() {
        return library.concat(name).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SymbolInfo) {
            SymbolInfo other = (SymbolInfo) obj;
            return library.equals(other.library) && name.equals(other.name);
        }
        return false;
    }
}
