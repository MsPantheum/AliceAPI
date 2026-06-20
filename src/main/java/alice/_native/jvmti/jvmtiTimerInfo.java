package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiTimerInfo extends NativeObject {

    public static final long SIZE = 32L;

    private static final long max_value_offset = 0L;
    private static final long may_skip_forward_offset = 8L;
    private static final long may_skip_backward_offset = 9L;
    private static final long kind_offset = 12L;
    private static final long reserved1_offset = 16L;
    private static final long reserved2_offset = 24L;

    public jvmtiTimerInfo(long address) {
        super(address);
    }

    public jvmtiTimerInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long max_value() {
        return Unsafe.getLong(address + max_value_offset);
    }

    public void max_value(long max_value) {
        Unsafe.putLong(address + max_value_offset, max_value);
    }

    public boolean may_skip_forward() {
        return Unsafe.getByte(address + may_skip_forward_offset) != 0;
    }

    public void may_skip_forward(boolean may_skip_forward) {
        Unsafe.putByte(address + may_skip_forward_offset, (byte) (may_skip_forward ? 1 : 0));
    }

    public boolean may_skip_backward() {
        return Unsafe.getByte(address + may_skip_backward_offset) != 0;
    }

    public void may_skip_backward(boolean may_skip_backward) {
        Unsafe.putByte(address + may_skip_backward_offset, (byte) (may_skip_backward ? 1 : 0));
    }

    public int kind() {
        return Unsafe.getInt(address + kind_offset);
    }

    public void kind(int kind) {
        Unsafe.putInt(address + kind_offset, kind);
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
}
