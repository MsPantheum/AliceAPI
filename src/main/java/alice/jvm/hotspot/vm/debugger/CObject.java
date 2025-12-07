package alice.jvm.hotspot.vm.debugger;

import alice.tools.UnsafeHelper;

public class CObject {
    private final long handle;

    public CObject(long handle) {
        this.handle = handle;
    }

    @Override
    public int hashCode() {
        return (int) handle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CObject) {
            return ((CObject) obj).handle == handle;
        }
        return false;
    }

    public long getCIntegerAt(long address, int numBytes, boolean isUnsigned) {
        return DebuggerUtilities.dataToCInteger(readBytes(address, numBytes), isUnsigned);
    }

    protected final byte[] readBytes(long address, int numBytes) {
        byte[] bytes = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            bytes[i] = UnsafeHelper.getByte(address + i);
        }
        return bytes;
    }
}
