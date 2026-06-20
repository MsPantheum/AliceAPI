package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiHeapReferenceInfoReserved extends NativeObject {

    public static final long SIZE = 64L;

    private static final long reserved1_offset = 0L;
    private static final long reserved2_offset = 8L;
    private static final long reserved3_offset = 16L;
    private static final long reserved4_offset = 24L;
    private static final long reserved5_offset = 32L;
    private static final long reserved6_offset = 40L;
    private static final long reserved7_offset = 48L;
    private static final long reserved8_offset = 56L;

    public jvmtiHeapReferenceInfoReserved(long address) {
        super(address);
    }

    public jvmtiHeapReferenceInfoReserved() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long reserved1() {
        return Unsafe.getLong(address + reserved1_offset);
    }

    public void reserved1(long reserved1) {
        Unsafe.putLong(address + reserved1_offset, reserved1);
    }

    public long reserved2() {
        return Unsafe.getLong(address + reserved2_offset);
    }

    public void reserved2(long reserved2) {
        Unsafe.putLong(address + reserved2_offset, reserved2);
    }

    public long reserved3() {
        return Unsafe.getLong(address + reserved3_offset);
    }

    public void reserved3(long reserved3) {
        Unsafe.putLong(address + reserved3_offset, reserved3);
    }

    public long reserved4() {
        return Unsafe.getLong(address + reserved4_offset);
    }

    public void reserved4(long reserved4) {
        Unsafe.putLong(address + reserved4_offset, reserved4);
    }

    public long reserved5() {
        return Unsafe.getLong(address + reserved5_offset);
    }

    public void reserved5(long reserved5) {
        Unsafe.putLong(address + reserved5_offset, reserved5);
    }

    public long reserved6() {
        return Unsafe.getLong(address + reserved6_offset);
    }

    public void reserved6(long reserved6) {
        Unsafe.putLong(address + reserved6_offset, reserved6);
    }

    public long reserved7() {
        return Unsafe.getLong(address + reserved7_offset);
    }

    public void reserved7(long reserved7) {
        Unsafe.putLong(address + reserved7_offset, reserved7);
    }

    public long reserved8() {
        return Unsafe.getLong(address + reserved8_offset);
    }

    public void reserved8(long reserved8) {
        Unsafe.putLong(address + reserved8_offset, reserved8);
    }
}
