package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiClassDefinition extends NativeObject {

    public static final long SIZE = 24L;

    private static final long klass_offset = 0L;
    private static final long class_byte_count_offset = 8L;
    private static final long class_bytes_offset = 16L;

    public jvmtiClassDefinition(long address) {
        super(address);
    }

    public jvmtiClassDefinition() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long klass() {
        return Unsafe.getLong(address + klass_offset);
    }

    public void klass(long klass) {
        Unsafe.putLong(address + klass_offset, klass);
    }

    public int class_byte_count() {
        return Unsafe.getInt(address + class_byte_count_offset);
    }

    public void class_byte_count(int class_byte_count) {
        Unsafe.putInt(address + class_byte_count_offset, class_byte_count);
    }

    public long class_bytes() {
        return Unsafe.getLong(address + class_bytes_offset);
    }

    public void class_bytes(long class_bytes) {
        Unsafe.putLong(address + class_bytes_offset, class_bytes);
    }
}
