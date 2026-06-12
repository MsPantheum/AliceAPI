package alice._native;

import alice.log.Logger;
import alice.util.Unsafe;

public abstract class NativeObject {
    public final long address;
    protected boolean dead = false;
    protected final boolean allocated;

    protected NativeObject(long address) {
        this.address = address;
        this.allocated = false;
    }

    protected NativeObject(long address, boolean allocated) {
        this.address = address;
        this.allocated = allocated;
    }

    protected NativeObject() {
        this.address = Unsafe.allocateMemory(getSize());
        this.allocated = true;
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

    public abstract long getSize();

    @Override
    public String toString() {
        return getClass().getSimpleName().concat("@0x").concat(Long.toHexString(address));
    }

    public void release() {
        if (!allocated) {
            throw new IllegalStateException("Attempt to release memory that isn't allocated by ourself!");
        }
        if (dead) {
            throw new IllegalStateException("Already released!");
        }
        Unsafe.freeMemory(address);
        dead = true;
    }

    @Override
    protected void finalize() throws Throwable {
        if (allocated && !dead) {
            Logger.MAIN.error("Memory leak detected! Someone allocated this but didn't release it when it was no longer referenced to!");
            Logger.MAIN.error("Address:0x" + Long.toHexString(address));
            Unsafe.freeMemory(address);
            dead = true;
        }
        super.finalize();
    }
}
