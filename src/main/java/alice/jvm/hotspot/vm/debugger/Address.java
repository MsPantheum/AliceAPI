package alice.jvm.hotspot.vm.debugger;

import alice.tools.UnsafeHelper;

public class Address {
    private final long handle;

    public Address(long handle) {
        this.handle = handle;
    }

    @Override
    public int hashCode() {
        return (int) handle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            return ((Address) obj).handle == handle;
        }
        return false;
    }

    public long getCIntegerAt(long offset, int numBytes, boolean isUnsigned) {
        return DebuggerUtilities.dataToCInteger(UnsafeHelper.readBytes(handle + offset, numBytes), isUnsigned);
    }

    public Address getCObjectAt(long offset) {
        return new Address(getCIntegerAt(offset, 8, true));
    }
}
