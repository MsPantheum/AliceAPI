package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiLocalVariableEntry extends NativeObject {

    public static final long SIZE = 48L;

    private static final long start_location_offset = 0L;
    private static final long length_offset = 8L;
    private static final long name_offset = 16L;
    private static final long signature_offset = 24L;
    private static final long generic_signature_offset = 32L;
    private static final long slot_offset = 40L;

    public jvmtiLocalVariableEntry(long address) {
        super(address);
    }

    public jvmtiLocalVariableEntry() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long start_location() {
        return Unsafe.getLong(address + start_location_offset);
    }

    public void start_location(long start_location) {
        Unsafe.putLong(address + start_location_offset, start_location);
    }

    public int length() {
        return Unsafe.getInt(address + length_offset);
    }

    public void length(int length) {
        Unsafe.putInt(address + length_offset, length);
    }

    public long name() {
        return Unsafe.getLong(address + name_offset);
    }

    public void name(long name) {
        Unsafe.putLong(address + name_offset, name);
    }

    public long signature() {
        return Unsafe.getLong(address + signature_offset);
    }

    public void signature(long signature) {
        Unsafe.putLong(address + signature_offset, signature);
    }

    public long generic_signature() {
        return Unsafe.getLong(address + generic_signature_offset);
    }

    public void generic_signature(long generic_signature) {
        Unsafe.putLong(address + generic_signature_offset, generic_signature);
    }

    public int slot() {
        return Unsafe.getInt(address + slot_offset);
    }

    public void slot(int slot) {
        Unsafe.putInt(address + slot_offset, slot);
    }
}
