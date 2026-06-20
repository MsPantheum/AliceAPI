package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiExtensionEventInfo extends NativeObject {

    public static final long SIZE = 40L;

    private static final long extension_event_index_offset = 0L;
    private static final long id_offset = 8L;
    private static final long short_description_offset = 16L;
    private static final long param_count_offset = 24L;
    private static final long params_offset = 32L;

    public jvmtiExtensionEventInfo(long address) {
        super(address);
    }

    public jvmtiExtensionEventInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public int extension_event_index() {
        return Unsafe.getInt(address + extension_event_index_offset);
    }

    public void extension_event_index(int extension_event_index) {
        Unsafe.putInt(address + extension_event_index_offset, extension_event_index);
    }

    public long id() {
        return Unsafe.getLong(address + id_offset);
    }

    public void id(long id) {
        Unsafe.putLong(address + id_offset, id);
    }

    public long short_description() {
        return Unsafe.getLong(address + short_description_offset);
    }

    public void short_description(long short_description) {
        Unsafe.putLong(address + short_description_offset, short_description);
    }

    public int param_count() {
        return Unsafe.getInt(address + param_count_offset);
    }

    public void param_count(int param_count) {
        Unsafe.putInt(address + param_count_offset, param_count);
    }

    public long params() {
        return Unsafe.getLong(address + params_offset);
    }

    public void params(long params) {
        Unsafe.putLong(address + params_offset, params);
    }
}
