package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiHeapReferenceInfoField extends NativeObject {

    public static final long SIZE = 4L;

    private static final long index_offset = 0L;

    public jvmtiHeapReferenceInfoField(long address) {
        super(address);
    }

    public jvmtiHeapReferenceInfoField() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public int index() {
        return Unsafe.getInt(address + index_offset);
    }

    public void index(int index) {
        Unsafe.putInt(address + index_offset, index);
    }
}
