package alice._native;

public abstract class NativeObject {
    public final long address;

    protected NativeObject(long address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        return (int) address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NativeObject) {
            return ((NativeObject) obj).address == this.address;
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().concat("@0x").concat(Long.toHexString(address));
    }
}
