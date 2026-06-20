package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiParamInfo extends NativeObject {

    public static final long SIZE = 24L;

    private static final long name_offset = 0L;
    private static final long kind_offset = 8L;
    private static final long base_type_offset = 12L;
    private static final long null_ok_offset = 16L;

    public jvmtiParamInfo(long address) {
        super(address);
    }

    public jvmtiParamInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long name() {
        return Unsafe.getLong(address + name_offset);
    }

    public void name(long name) {
        Unsafe.putLong(address + name_offset, name);
    }

    public int kind() {
        return Unsafe.getInt(address + kind_offset);
    }

    public void kind(int kind) {
        Unsafe.putInt(address + kind_offset, kind);
    }

    public int base_type() {
        return Unsafe.getInt(address + base_type_offset);
    }

    public void base_type(int base_type) {
        Unsafe.putInt(address + base_type_offset, base_type);
    }

    public boolean null_ok() {
        return Unsafe.getByte(address + null_ok_offset) != 0;
    }

    public void null_ok(boolean null_ok) {
        Unsafe.putByte(address + null_ok_offset, (byte) (null_ok ? 1 : 0));
    }
}
