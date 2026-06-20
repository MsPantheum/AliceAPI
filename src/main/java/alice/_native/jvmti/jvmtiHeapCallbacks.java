package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiHeapCallbacks extends NativeObject {

    public static final long SIZE = 128L;

    private static final long heap_iteration_callback_offset = 0L;
    private static final long heap_reference_callback_offset = 8L;
    private static final long primitive_field_callback_offset = 16L;
    private static final long array_primitive_value_callback_offset = 24L;
    private static final long string_primitive_value_callback_offset = 32L;
    private static final long reserved5_offset = 40L;
    private static final long reserved6_offset = 48L;
    private static final long reserved7_offset = 56L;
    private static final long reserved8_offset = 64L;
    private static final long reserved9_offset = 72L;
    private static final long reserved10_offset = 80L;
    private static final long reserved11_offset = 88L;
    private static final long reserved12_offset = 96L;
    private static final long reserved13_offset = 104L;
    private static final long reserved14_offset = 112L;
    private static final long reserved15_offset = 120L;

    public jvmtiHeapCallbacks(long address) {
        super(address);
    }

    public jvmtiHeapCallbacks() {
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public long heap_iteration_callback() {
        return Unsafe.getLong(address + heap_iteration_callback_offset);
    }

    public void heap_iteration_callback(long heap_iteration_callback) {
        Unsafe.putLong(address + heap_iteration_callback_offset, heap_iteration_callback);
    }

    public long heap_reference_callback() {
        return Unsafe.getLong(address + heap_reference_callback_offset);
    }

    public void heap_reference_callback(long heap_reference_callback) {
        Unsafe.putLong(address + heap_reference_callback_offset, heap_reference_callback);
    }

    public long primitive_field_callback() {
        return Unsafe.getLong(address + primitive_field_callback_offset);
    }

    public void primitive_field_callback(long primitive_field_callback) {
        Unsafe.putLong(address + primitive_field_callback_offset, primitive_field_callback);
    }

    public long array_primitive_value_callback() {
        return Unsafe.getLong(address + array_primitive_value_callback_offset);
    }

    public void array_primitive_value_callback(long array_primitive_value_callback) {
        Unsafe.putLong(address + array_primitive_value_callback_offset, array_primitive_value_callback);
    }

    public long string_primitive_value_callback() {
        return Unsafe.getLong(address + string_primitive_value_callback_offset);
    }

    public void string_primitive_value_callback(long string_primitive_value_callback) {
        Unsafe.putLong(address + string_primitive_value_callback_offset, string_primitive_value_callback);
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

    public long reserved9() {
        return Unsafe.getLong(address + reserved9_offset);
    }

    public void reserved9(long reserved9) {
        Unsafe.putLong(address + reserved9_offset, reserved9);
    }

    public long reserved10() {
        return Unsafe.getLong(address + reserved10_offset);
    }

    public void reserved10(long reserved10) {
        Unsafe.putLong(address + reserved10_offset, reserved10);
    }

    public long reserved11() {
        return Unsafe.getLong(address + reserved11_offset);
    }

    public void reserved11(long reserved11) {
        Unsafe.putLong(address + reserved11_offset, reserved11);
    }

    public long reserved12() {
        return Unsafe.getLong(address + reserved12_offset);
    }

    public void reserved12(long reserved12) {
        Unsafe.putLong(address + reserved12_offset, reserved12);
    }

    public long reserved13() {
        return Unsafe.getLong(address + reserved13_offset);
    }

    public void reserved13(long reserved13) {
        Unsafe.putLong(address + reserved13_offset, reserved13);
    }

    public long reserved14() {
        return Unsafe.getLong(address + reserved14_offset);
    }

    public void reserved14(long reserved14) {
        Unsafe.putLong(address + reserved14_offset, reserved14);
    }

    public long reserved15() {
        return Unsafe.getLong(address + reserved15_offset);
    }

    public void reserved15(long reserved15) {
        Unsafe.putLong(address + reserved15_offset, reserved15);
    }
}
