package alice;

import alice._native.jvmti.jvmtiCapabilities;
import alice._native.jvmti.jvmtiInterface_1_.AddCapabilities;
import alice.exception.JNIException;

public class Test11 {
    public static void main(String[] args) {
        Init.ensureInit();
        jvmtiCapabilities caps = new jvmtiCapabilities();
        try {
            caps.clear();
            caps.can_generate_compiled_method_load_events(true);
            caps.can_generate_all_class_hook_events(true);
            caps.can_generate_native_method_bind_events(true);
            caps.can_set_native_method_prefix(true);
            int ret = AddCapabilities.invoke(caps.address);
            if (ret != 0) {
                throw new JNIException("Failed to add JVMTI capabilities!", ret);
            }

        } finally {
            caps.release();
        }
    }
}
