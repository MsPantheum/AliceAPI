package alice._native.win32;

import alice._native.NativeObject;
import alice.util.AddressUtil;
import alice.util.Unsafe;

//typedef struct _MEMORY_BASIC_INFORMATION {
//  PVOID  BaseAddress;
//  PVOID  AllocationBase;
//  DWORD  AllocationProtect;
//  WORD   PartitionId;
//  SIZE_T RegionSize;
//  DWORD  State;
//  DWORD  Protect;
//  DWORD  Type;
//} MEMORY_BASIC_INFORMATION, *PMEMORY_BASIC_INFORMATION;

public class MEMORY_BASIC_INFORMATION extends NativeObject {

    public static final long SIZE;

    private static final long BaseAddress_offset = 0;
    private static final long AllocationBase_offset = BaseAddress_offset + Unsafe.ADDRESS_SIZE;
    private static final long AllocationProtect_offset = AllocationBase_offset + Unsafe.ADDRESS_SIZE;
    private static final long PartitionId_offset = AllocationProtect_offset + 4;
    private static final long RegionSize_offset = (PartitionId_offset + 2) % Unsafe.ADDRESS_SIZE == 0 ? (PartitionId_offset + 2) : AddressUtil.align(PartitionId_offset + 2);
    private static final long State_offset = RegionSize_offset + Unsafe.ADDRESS_SIZE;
    private static final long Protect_offset = State_offset + 4;
    private static final long Type_offset = Protect_offset + 4;


    static {
        SIZE = AddressUtil.align(Unsafe.ADDRESS_SIZE * 3L + 2 + 4 * 4);
    }

    public MEMORY_BASIC_INFORMATION(long address) {
        super(address);
    }

    public MEMORY_BASIC_INFORMATION() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long BaseAddress() {
        return Unsafe.getLong(address + BaseAddress_offset);
    }

    public long AllocationBase() {
        return Unsafe.getLong(address + AllocationBase_offset);
    }

    public int AllocationProtect() {
        return Unsafe.getInt(address + AllocationProtect_offset);
    }

    public short PartitionId() {
        return Unsafe.getShort(address + PartitionId_offset);
    }

    public long RegionSize() {
        return Unsafe.getLong(address + RegionSize_offset);
    }

    public int State() {
        return Unsafe.getInt(address + State_offset);
    }

    public int Protect() {
        return Unsafe.getInt(address + Protect_offset);
    }

    public int Type() {
        return Unsafe.getInt(address + Type_offset);
    }
}
