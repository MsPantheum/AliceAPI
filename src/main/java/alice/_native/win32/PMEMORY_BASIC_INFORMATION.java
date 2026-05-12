package alice._native.win32;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class PMEMORY_BASIC_INFORMATION extends NativeObject {

    public static final long SIZE;

    static {
        SIZE = Unsafe.ADDRESS_SIZE * 3L + 2 + 4 * 4;
    }

    public static PMEMORY_BASIC_INFORMATION allocate() {
        long address = Unsafe.allocateMemory(SIZE);
        return new PMEMORY_BASIC_INFORMATION(address);
    }

    public PMEMORY_BASIC_INFORMATION(long address) {
        super(address);
    }

    public long BaseAddress() {
        return Unsafe.getLong(address);
    }

    public long AllocationBase() {
        return Unsafe.getLong(address + Unsafe.ADDRESS_SIZE);
    }

    public int AllocationProtect() {
        return Unsafe.getInt(address + Unsafe.ADDRESS_SIZE * 2L);
    }

    public short PartitionId() {
        return Unsafe.getShort(address + Unsafe.ADDRESS_SIZE * 2L + 4);
    }

    public long RegionSize() {
        return Unsafe.getLong(address + Unsafe.ADDRESS_SIZE * 2L + 6);
    }

    public int State() {
        return Unsafe.getInt(address + Unsafe.ADDRESS_SIZE * 3L + 6);
    }

    public int Protect() {
        return Unsafe.getInt(address + Unsafe.ADDRESS_SIZE * 3L + 10);
    }

    public int Type() {
        return Unsafe.getInt(address + Unsafe.ADDRESS_SIZE * 3L + 14);
    }
}
