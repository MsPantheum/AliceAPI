package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiExtensionFunctionInfo extends NativeObject {

    public static final long SIZE = 56L;

    private static final long func_offset = 0L;
    private static final long id_offset = 8L;
    private static final long short_description_offset = 16L;
    private static final long param_count_offset = 24L;
    private static final long params_offset = 32L;
    private static final long error_count_offset = 40L;
    private static final long errors_offset = 48L;

    public jvmtiExtensionFunctionInfo(long address) {
        super(address);
    }

    public jvmtiExtensionFunctionInfo() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long func() {
        return Unsafe.getLong(address + func_offset);
    }

    public void func(long func) {
        Unsafe.putLong(address + func_offset, func);
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

    public int error_count() {
        return Unsafe.getInt(address + error_count_offset);
    }

    public void error_count(int error_count) {
        Unsafe.putInt(address + error_count_offset, error_count);
    }

    public long errors() {
        return Unsafe.getLong(address + errors_offset);
    }

    public void errors(long errors) {
        Unsafe.putLong(address + errors_offset, errors);
    }
}
