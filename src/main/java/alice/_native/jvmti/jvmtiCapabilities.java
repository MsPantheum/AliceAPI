package alice._native.jvmti;

import alice._native.NativeObject;
import alice.util.Unsafe;

public class jvmtiCapabilities extends NativeObject {

    public static final long SIZE = 16L;

    private static final int can_tag_objects_word = 0;
    private static final int can_tag_objects_mask = 1 << 0;
    private static final int can_generate_field_modification_events_word = 0;
    private static final int can_generate_field_modification_events_mask = 1 << 1;
    private static final int can_generate_field_access_events_word = 0;
    private static final int can_generate_field_access_events_mask = 1 << 2;
    private static final int can_get_bytecodes_word = 0;
    private static final int can_get_bytecodes_mask = 1 << 3;
    private static final int can_get_synthetic_attribute_word = 0;
    private static final int can_get_synthetic_attribute_mask = 1 << 4;
    private static final int can_get_owned_monitor_info_word = 0;
    private static final int can_get_owned_monitor_info_mask = 1 << 5;
    private static final int can_get_current_contended_monitor_word = 0;
    private static final int can_get_current_contended_monitor_mask = 1 << 6;
    private static final int can_get_monitor_info_word = 0;
    private static final int can_get_monitor_info_mask = 1 << 7;
    private static final int can_pop_frame_word = 0;
    private static final int can_pop_frame_mask = 1 << 8;
    private static final int can_redefine_classes_word = 0;
    private static final int can_redefine_classes_mask = 1 << 9;
    private static final int can_signal_thread_word = 0;
    private static final int can_signal_thread_mask = 1 << 10;
    private static final int can_get_source_file_name_word = 0;
    private static final int can_get_source_file_name_mask = 1 << 11;
    private static final int can_get_line_numbers_word = 0;
    private static final int can_get_line_numbers_mask = 1 << 12;
    private static final int can_get_source_debug_extension_word = 0;
    private static final int can_get_source_debug_extension_mask = 1 << 13;
    private static final int can_access_local_variables_word = 0;
    private static final int can_access_local_variables_mask = 1 << 14;
    private static final int can_maintain_original_method_order_word = 0;
    private static final int can_maintain_original_method_order_mask = 1 << 15;
    private static final int can_generate_single_step_events_word = 0;
    private static final int can_generate_single_step_events_mask = 1 << 16;
    private static final int can_generate_exception_events_word = 0;
    private static final int can_generate_exception_events_mask = 1 << 17;
    private static final int can_generate_frame_pop_events_word = 0;
    private static final int can_generate_frame_pop_events_mask = 1 << 18;
    private static final int can_generate_breakpoint_events_word = 0;
    private static final int can_generate_breakpoint_events_mask = 1 << 19;
    private static final int can_suspend_word = 0;
    private static final int can_suspend_mask = 1 << 20;
    private static final int can_redefine_any_class_word = 0;
    private static final int can_redefine_any_class_mask = 1 << 21;
    private static final int can_get_current_thread_cpu_time_word = 0;
    private static final int can_get_current_thread_cpu_time_mask = 1 << 22;
    private static final int can_get_thread_cpu_time_word = 0;
    private static final int can_get_thread_cpu_time_mask = 1 << 23;
    private static final int can_generate_method_entry_events_word = 0;
    private static final int can_generate_method_entry_events_mask = 1 << 24;
    private static final int can_generate_method_exit_events_word = 0;
    private static final int can_generate_method_exit_events_mask = 1 << 25;
    private static final int can_generate_all_class_hook_events_word = 0;
    private static final int can_generate_all_class_hook_events_mask = 1 << 26;
    private static final int can_generate_compiled_method_load_events_word = 0;
    private static final int can_generate_compiled_method_load_events_mask = 1 << 27;
    private static final int can_generate_monitor_events_word = 0;
    private static final int can_generate_monitor_events_mask = 1 << 28;
    private static final int can_generate_vm_object_alloc_events_word = 0;
    private static final int can_generate_vm_object_alloc_events_mask = 1 << 29;
    private static final int can_generate_native_method_bind_events_word = 0;
    private static final int can_generate_native_method_bind_events_mask = 1 << 30;
    private static final int can_generate_garbage_collection_events_word = 0;
    private static final int can_generate_garbage_collection_events_mask = 1 << 31;
    private static final int can_generate_object_free_events_word = 1;
    private static final int can_generate_object_free_events_mask = 1 << 0;
    private static final int can_force_early_return_word = 1;
    private static final int can_force_early_return_mask = 1 << 1;
    private static final int can_get_owned_monitor_stack_depth_info_word = 1;
    private static final int can_get_owned_monitor_stack_depth_info_mask = 1 << 2;
    private static final int can_get_constant_pool_word = 1;
    private static final int can_get_constant_pool_mask = 1 << 3;
    private static final int can_set_native_method_prefix_word = 1;
    private static final int can_set_native_method_prefix_mask = 1 << 4;
    private static final int can_retransform_classes_word = 1;
    private static final int can_retransform_classes_mask = 1 << 5;
    private static final int can_retransform_any_class_word = 1;
    private static final int can_retransform_any_class_mask = 1 << 6;
    private static final int can_generate_resource_exhaustion_heap_events_word = 1;
    private static final int can_generate_resource_exhaustion_heap_events_mask = 1 << 7;
    private static final int can_generate_resource_exhaustion_threads_events_word = 1;
    private static final int can_generate_resource_exhaustion_threads_events_mask = 1 << 8;

    public jvmtiCapabilities(long address) {
        super(address);
    }

    public jvmtiCapabilities() {
        super();
    }

    @Override
    public long getSize() {
        return SIZE;
    }

    public int word(int index) {
        return Unsafe.getInt(address + (long) index * Integer.BYTES);
    }

    public void word(int index, int value) {
        Unsafe.putInt(address + (long) index * Integer.BYTES, value);
    }

    public void clear() {
        for (long offset = 0; offset < SIZE; offset += Integer.BYTES) {
            Unsafe.putInt(address + offset, 0);
        }
    }

    public boolean can_tag_objects() {
        return (word(can_tag_objects_word) & can_tag_objects_mask) != 0;
    }

    public void can_tag_objects(boolean can_tag_objects) {
        int value = word(can_tag_objects_word);
        if (can_tag_objects) {
            value |= can_tag_objects_mask;
        } else {
            value &= ~can_tag_objects_mask;
        }
        word(can_tag_objects_word, value);
    }

    public boolean can_generate_field_modification_events() {
        return (word(can_generate_field_modification_events_word) & can_generate_field_modification_events_mask) != 0;
    }

    public void can_generate_field_modification_events(boolean can_generate_field_modification_events) {
        int value = word(can_generate_field_modification_events_word);
        if (can_generate_field_modification_events) {
            value |= can_generate_field_modification_events_mask;
        } else {
            value &= ~can_generate_field_modification_events_mask;
        }
        word(can_generate_field_modification_events_word, value);
    }

    public boolean can_generate_field_access_events() {
        return (word(can_generate_field_access_events_word) & can_generate_field_access_events_mask) != 0;
    }

    public void can_generate_field_access_events(boolean can_generate_field_access_events) {
        int value = word(can_generate_field_access_events_word);
        if (can_generate_field_access_events) {
            value |= can_generate_field_access_events_mask;
        } else {
            value &= ~can_generate_field_access_events_mask;
        }
        word(can_generate_field_access_events_word, value);
    }

    public boolean can_get_bytecodes() {
        return (word(can_get_bytecodes_word) & can_get_bytecodes_mask) != 0;
    }

    public void can_get_bytecodes(boolean can_get_bytecodes) {
        int value = word(can_get_bytecodes_word);
        if (can_get_bytecodes) {
            value |= can_get_bytecodes_mask;
        } else {
            value &= ~can_get_bytecodes_mask;
        }
        word(can_get_bytecodes_word, value);
    }

    public boolean can_get_synthetic_attribute() {
        return (word(can_get_synthetic_attribute_word) & can_get_synthetic_attribute_mask) != 0;
    }

    public void can_get_synthetic_attribute(boolean can_get_synthetic_attribute) {
        int value = word(can_get_synthetic_attribute_word);
        if (can_get_synthetic_attribute) {
            value |= can_get_synthetic_attribute_mask;
        } else {
            value &= ~can_get_synthetic_attribute_mask;
        }
        word(can_get_synthetic_attribute_word, value);
    }

    public boolean can_get_owned_monitor_info() {
        return (word(can_get_owned_monitor_info_word) & can_get_owned_monitor_info_mask) != 0;
    }

    public void can_get_owned_monitor_info(boolean can_get_owned_monitor_info) {
        int value = word(can_get_owned_monitor_info_word);
        if (can_get_owned_monitor_info) {
            value |= can_get_owned_monitor_info_mask;
        } else {
            value &= ~can_get_owned_monitor_info_mask;
        }
        word(can_get_owned_monitor_info_word, value);
    }

    public boolean can_get_current_contended_monitor() {
        return (word(can_get_current_contended_monitor_word) & can_get_current_contended_monitor_mask) != 0;
    }

    public void can_get_current_contended_monitor(boolean can_get_current_contended_monitor) {
        int value = word(can_get_current_contended_monitor_word);
        if (can_get_current_contended_monitor) {
            value |= can_get_current_contended_monitor_mask;
        } else {
            value &= ~can_get_current_contended_monitor_mask;
        }
        word(can_get_current_contended_monitor_word, value);
    }

    public boolean can_get_monitor_info() {
        return (word(can_get_monitor_info_word) & can_get_monitor_info_mask) != 0;
    }

    public void can_get_monitor_info(boolean can_get_monitor_info) {
        int value = word(can_get_monitor_info_word);
        if (can_get_monitor_info) {
            value |= can_get_monitor_info_mask;
        } else {
            value &= ~can_get_monitor_info_mask;
        }
        word(can_get_monitor_info_word, value);
    }

    public boolean can_pop_frame() {
        return (word(can_pop_frame_word) & can_pop_frame_mask) != 0;
    }

    public void can_pop_frame(boolean can_pop_frame) {
        int value = word(can_pop_frame_word);
        if (can_pop_frame) {
            value |= can_pop_frame_mask;
        } else {
            value &= ~can_pop_frame_mask;
        }
        word(can_pop_frame_word, value);
    }

    public boolean can_redefine_classes() {
        return (word(can_redefine_classes_word) & can_redefine_classes_mask) != 0;
    }

    public void can_redefine_classes(boolean can_redefine_classes) {
        int value = word(can_redefine_classes_word);
        if (can_redefine_classes) {
            value |= can_redefine_classes_mask;
        } else {
            value &= ~can_redefine_classes_mask;
        }
        word(can_redefine_classes_word, value);
    }

    public boolean can_signal_thread() {
        return (word(can_signal_thread_word) & can_signal_thread_mask) != 0;
    }

    public void can_signal_thread(boolean can_signal_thread) {
        int value = word(can_signal_thread_word);
        if (can_signal_thread) {
            value |= can_signal_thread_mask;
        } else {
            value &= ~can_signal_thread_mask;
        }
        word(can_signal_thread_word, value);
    }

    public boolean can_get_source_file_name() {
        return (word(can_get_source_file_name_word) & can_get_source_file_name_mask) != 0;
    }

    public void can_get_source_file_name(boolean can_get_source_file_name) {
        int value = word(can_get_source_file_name_word);
        if (can_get_source_file_name) {
            value |= can_get_source_file_name_mask;
        } else {
            value &= ~can_get_source_file_name_mask;
        }
        word(can_get_source_file_name_word, value);
    }

    public boolean can_get_line_numbers() {
        return (word(can_get_line_numbers_word) & can_get_line_numbers_mask) != 0;
    }

    public void can_get_line_numbers(boolean can_get_line_numbers) {
        int value = word(can_get_line_numbers_word);
        if (can_get_line_numbers) {
            value |= can_get_line_numbers_mask;
        } else {
            value &= ~can_get_line_numbers_mask;
        }
        word(can_get_line_numbers_word, value);
    }

    public boolean can_get_source_debug_extension() {
        return (word(can_get_source_debug_extension_word) & can_get_source_debug_extension_mask) != 0;
    }

    public void can_get_source_debug_extension(boolean can_get_source_debug_extension) {
        int value = word(can_get_source_debug_extension_word);
        if (can_get_source_debug_extension) {
            value |= can_get_source_debug_extension_mask;
        } else {
            value &= ~can_get_source_debug_extension_mask;
        }
        word(can_get_source_debug_extension_word, value);
    }

    public boolean can_access_local_variables() {
        return (word(can_access_local_variables_word) & can_access_local_variables_mask) != 0;
    }

    public void can_access_local_variables(boolean can_access_local_variables) {
        int value = word(can_access_local_variables_word);
        if (can_access_local_variables) {
            value |= can_access_local_variables_mask;
        } else {
            value &= ~can_access_local_variables_mask;
        }
        word(can_access_local_variables_word, value);
    }

    public boolean can_maintain_original_method_order() {
        return (word(can_maintain_original_method_order_word) & can_maintain_original_method_order_mask) != 0;
    }

    public void can_maintain_original_method_order(boolean can_maintain_original_method_order) {
        int value = word(can_maintain_original_method_order_word);
        if (can_maintain_original_method_order) {
            value |= can_maintain_original_method_order_mask;
        } else {
            value &= ~can_maintain_original_method_order_mask;
        }
        word(can_maintain_original_method_order_word, value);
    }

    public boolean can_generate_single_step_events() {
        return (word(can_generate_single_step_events_word) & can_generate_single_step_events_mask) != 0;
    }

    public void can_generate_single_step_events(boolean can_generate_single_step_events) {
        int value = word(can_generate_single_step_events_word);
        if (can_generate_single_step_events) {
            value |= can_generate_single_step_events_mask;
        } else {
            value &= ~can_generate_single_step_events_mask;
        }
        word(can_generate_single_step_events_word, value);
    }

    public boolean can_generate_exception_events() {
        return (word(can_generate_exception_events_word) & can_generate_exception_events_mask) != 0;
    }

    public void can_generate_exception_events(boolean can_generate_exception_events) {
        int value = word(can_generate_exception_events_word);
        if (can_generate_exception_events) {
            value |= can_generate_exception_events_mask;
        } else {
            value &= ~can_generate_exception_events_mask;
        }
        word(can_generate_exception_events_word, value);
    }

    public boolean can_generate_frame_pop_events() {
        return (word(can_generate_frame_pop_events_word) & can_generate_frame_pop_events_mask) != 0;
    }

    public void can_generate_frame_pop_events(boolean can_generate_frame_pop_events) {
        int value = word(can_generate_frame_pop_events_word);
        if (can_generate_frame_pop_events) {
            value |= can_generate_frame_pop_events_mask;
        } else {
            value &= ~can_generate_frame_pop_events_mask;
        }
        word(can_generate_frame_pop_events_word, value);
    }

    public boolean can_generate_breakpoint_events() {
        return (word(can_generate_breakpoint_events_word) & can_generate_breakpoint_events_mask) != 0;
    }

    public void can_generate_breakpoint_events(boolean can_generate_breakpoint_events) {
        int value = word(can_generate_breakpoint_events_word);
        if (can_generate_breakpoint_events) {
            value |= can_generate_breakpoint_events_mask;
        } else {
            value &= ~can_generate_breakpoint_events_mask;
        }
        word(can_generate_breakpoint_events_word, value);
    }

    public boolean can_suspend() {
        return (word(can_suspend_word) & can_suspend_mask) != 0;
    }

    public void can_suspend(boolean can_suspend) {
        int value = word(can_suspend_word);
        if (can_suspend) {
            value |= can_suspend_mask;
        } else {
            value &= ~can_suspend_mask;
        }
        word(can_suspend_word, value);
    }

    public boolean can_redefine_any_class() {
        return (word(can_redefine_any_class_word) & can_redefine_any_class_mask) != 0;
    }

    public void can_redefine_any_class(boolean can_redefine_any_class) {
        int value = word(can_redefine_any_class_word);
        if (can_redefine_any_class) {
            value |= can_redefine_any_class_mask;
        } else {
            value &= ~can_redefine_any_class_mask;
        }
        word(can_redefine_any_class_word, value);
    }

    public boolean can_get_current_thread_cpu_time() {
        return (word(can_get_current_thread_cpu_time_word) & can_get_current_thread_cpu_time_mask) != 0;
    }

    public void can_get_current_thread_cpu_time(boolean can_get_current_thread_cpu_time) {
        int value = word(can_get_current_thread_cpu_time_word);
        if (can_get_current_thread_cpu_time) {
            value |= can_get_current_thread_cpu_time_mask;
        } else {
            value &= ~can_get_current_thread_cpu_time_mask;
        }
        word(can_get_current_thread_cpu_time_word, value);
    }

    public boolean can_get_thread_cpu_time() {
        return (word(can_get_thread_cpu_time_word) & can_get_thread_cpu_time_mask) != 0;
    }

    public void can_get_thread_cpu_time(boolean can_get_thread_cpu_time) {
        int value = word(can_get_thread_cpu_time_word);
        if (can_get_thread_cpu_time) {
            value |= can_get_thread_cpu_time_mask;
        } else {
            value &= ~can_get_thread_cpu_time_mask;
        }
        word(can_get_thread_cpu_time_word, value);
    }

    public boolean can_generate_method_entry_events() {
        return (word(can_generate_method_entry_events_word) & can_generate_method_entry_events_mask) != 0;
    }

    public void can_generate_method_entry_events(boolean can_generate_method_entry_events) {
        int value = word(can_generate_method_entry_events_word);
        if (can_generate_method_entry_events) {
            value |= can_generate_method_entry_events_mask;
        } else {
            value &= ~can_generate_method_entry_events_mask;
        }
        word(can_generate_method_entry_events_word, value);
    }

    public boolean can_generate_method_exit_events() {
        return (word(can_generate_method_exit_events_word) & can_generate_method_exit_events_mask) != 0;
    }

    public void can_generate_method_exit_events(boolean can_generate_method_exit_events) {
        int value = word(can_generate_method_exit_events_word);
        if (can_generate_method_exit_events) {
            value |= can_generate_method_exit_events_mask;
        } else {
            value &= ~can_generate_method_exit_events_mask;
        }
        word(can_generate_method_exit_events_word, value);
    }

    public boolean can_generate_all_class_hook_events() {
        return (word(can_generate_all_class_hook_events_word) & can_generate_all_class_hook_events_mask) != 0;
    }

    public void can_generate_all_class_hook_events(boolean can_generate_all_class_hook_events) {
        int value = word(can_generate_all_class_hook_events_word);
        if (can_generate_all_class_hook_events) {
            value |= can_generate_all_class_hook_events_mask;
        } else {
            value &= ~can_generate_all_class_hook_events_mask;
        }
        word(can_generate_all_class_hook_events_word, value);
    }

    public boolean can_generate_compiled_method_load_events() {
        return (word(can_generate_compiled_method_load_events_word) & can_generate_compiled_method_load_events_mask) != 0;
    }

    public void can_generate_compiled_method_load_events(boolean can_generate_compiled_method_load_events) {
        int value = word(can_generate_compiled_method_load_events_word);
        if (can_generate_compiled_method_load_events) {
            value |= can_generate_compiled_method_load_events_mask;
        } else {
            value &= ~can_generate_compiled_method_load_events_mask;
        }
        word(can_generate_compiled_method_load_events_word, value);
    }

    public boolean can_generate_monitor_events() {
        return (word(can_generate_monitor_events_word) & can_generate_monitor_events_mask) != 0;
    }

    public void can_generate_monitor_events(boolean can_generate_monitor_events) {
        int value = word(can_generate_monitor_events_word);
        if (can_generate_monitor_events) {
            value |= can_generate_monitor_events_mask;
        } else {
            value &= ~can_generate_monitor_events_mask;
        }
        word(can_generate_monitor_events_word, value);
    }

    public boolean can_generate_vm_object_alloc_events() {
        return (word(can_generate_vm_object_alloc_events_word) & can_generate_vm_object_alloc_events_mask) != 0;
    }

    public void can_generate_vm_object_alloc_events(boolean can_generate_vm_object_alloc_events) {
        int value = word(can_generate_vm_object_alloc_events_word);
        if (can_generate_vm_object_alloc_events) {
            value |= can_generate_vm_object_alloc_events_mask;
        } else {
            value &= ~can_generate_vm_object_alloc_events_mask;
        }
        word(can_generate_vm_object_alloc_events_word, value);
    }

    public boolean can_generate_native_method_bind_events() {
        return (word(can_generate_native_method_bind_events_word) & can_generate_native_method_bind_events_mask) != 0;
    }

    public void can_generate_native_method_bind_events(boolean can_generate_native_method_bind_events) {
        int value = word(can_generate_native_method_bind_events_word);
        if (can_generate_native_method_bind_events) {
            value |= can_generate_native_method_bind_events_mask;
        } else {
            value &= ~can_generate_native_method_bind_events_mask;
        }
        word(can_generate_native_method_bind_events_word, value);
    }

    public boolean can_generate_garbage_collection_events() {
        return (word(can_generate_garbage_collection_events_word) & can_generate_garbage_collection_events_mask) != 0;
    }

    public void can_generate_garbage_collection_events(boolean can_generate_garbage_collection_events) {
        int value = word(can_generate_garbage_collection_events_word);
        if (can_generate_garbage_collection_events) {
            value |= can_generate_garbage_collection_events_mask;
        } else {
            value &= ~can_generate_garbage_collection_events_mask;
        }
        word(can_generate_garbage_collection_events_word, value);
    }

    public boolean can_generate_object_free_events() {
        return (word(can_generate_object_free_events_word) & can_generate_object_free_events_mask) != 0;
    }

    public void can_generate_object_free_events(boolean can_generate_object_free_events) {
        int value = word(can_generate_object_free_events_word);
        if (can_generate_object_free_events) {
            value |= can_generate_object_free_events_mask;
        } else {
            value &= ~can_generate_object_free_events_mask;
        }
        word(can_generate_object_free_events_word, value);
    }

    public boolean can_force_early_return() {
        return (word(can_force_early_return_word) & can_force_early_return_mask) != 0;
    }

    public void can_force_early_return(boolean can_force_early_return) {
        int value = word(can_force_early_return_word);
        if (can_force_early_return) {
            value |= can_force_early_return_mask;
        } else {
            value &= ~can_force_early_return_mask;
        }
        word(can_force_early_return_word, value);
    }

    public boolean can_get_owned_monitor_stack_depth_info() {
        return (word(can_get_owned_monitor_stack_depth_info_word) & can_get_owned_monitor_stack_depth_info_mask) != 0;
    }

    public void can_get_owned_monitor_stack_depth_info(boolean can_get_owned_monitor_stack_depth_info) {
        int value = word(can_get_owned_monitor_stack_depth_info_word);
        if (can_get_owned_monitor_stack_depth_info) {
            value |= can_get_owned_monitor_stack_depth_info_mask;
        } else {
            value &= ~can_get_owned_monitor_stack_depth_info_mask;
        }
        word(can_get_owned_monitor_stack_depth_info_word, value);
    }

    public boolean can_get_constant_pool() {
        return (word(can_get_constant_pool_word) & can_get_constant_pool_mask) != 0;
    }

    public void can_get_constant_pool(boolean can_get_constant_pool) {
        int value = word(can_get_constant_pool_word);
        if (can_get_constant_pool) {
            value |= can_get_constant_pool_mask;
        } else {
            value &= ~can_get_constant_pool_mask;
        }
        word(can_get_constant_pool_word, value);
    }

    public boolean can_set_native_method_prefix() {
        return (word(can_set_native_method_prefix_word) & can_set_native_method_prefix_mask) != 0;
    }

    public void can_set_native_method_prefix(boolean can_set_native_method_prefix) {
        int value = word(can_set_native_method_prefix_word);
        if (can_set_native_method_prefix) {
            value |= can_set_native_method_prefix_mask;
        } else {
            value &= ~can_set_native_method_prefix_mask;
        }
        word(can_set_native_method_prefix_word, value);
    }

    public boolean can_retransform_classes() {
        return (word(can_retransform_classes_word) & can_retransform_classes_mask) != 0;
    }

    public void can_retransform_classes(boolean can_retransform_classes) {
        int value = word(can_retransform_classes_word);
        if (can_retransform_classes) {
            value |= can_retransform_classes_mask;
        } else {
            value &= ~can_retransform_classes_mask;
        }
        word(can_retransform_classes_word, value);
    }

    public boolean can_retransform_any_class() {
        return (word(can_retransform_any_class_word) & can_retransform_any_class_mask) != 0;
    }

    public void can_retransform_any_class(boolean can_retransform_any_class) {
        int value = word(can_retransform_any_class_word);
        if (can_retransform_any_class) {
            value |= can_retransform_any_class_mask;
        } else {
            value &= ~can_retransform_any_class_mask;
        }
        word(can_retransform_any_class_word, value);
    }

    public boolean can_generate_resource_exhaustion_heap_events() {
        return (word(can_generate_resource_exhaustion_heap_events_word) & can_generate_resource_exhaustion_heap_events_mask) != 0;
    }

    public void can_generate_resource_exhaustion_heap_events(boolean can_generate_resource_exhaustion_heap_events) {
        int value = word(can_generate_resource_exhaustion_heap_events_word);
        if (can_generate_resource_exhaustion_heap_events) {
            value |= can_generate_resource_exhaustion_heap_events_mask;
        } else {
            value &= ~can_generate_resource_exhaustion_heap_events_mask;
        }
        word(can_generate_resource_exhaustion_heap_events_word, value);
    }

    public boolean can_generate_resource_exhaustion_threads_events() {
        return (word(can_generate_resource_exhaustion_threads_events_word) & can_generate_resource_exhaustion_threads_events_mask) != 0;
    }

    public void can_generate_resource_exhaustion_threads_events(boolean can_generate_resource_exhaustion_threads_events) {
        int value = word(can_generate_resource_exhaustion_threads_events_word);
        if (can_generate_resource_exhaustion_threads_events) {
            value |= can_generate_resource_exhaustion_threads_events_mask;
        } else {
            value &= ~can_generate_resource_exhaustion_threads_events_mask;
        }
        word(can_generate_resource_exhaustion_threads_events_word, value);
    }
}
