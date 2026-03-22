package alice.util.constants;

import static sun.jvm.hotspot.runtime.ClassConstants.JVM_RECOGNIZED_FIELD_MODIFIERS;

public interface AccessFlags {
    int JVM_ACC_WRITTEN_FLAGS = 0x00007FFF,

    // Method* flags
    JVM_ACC_MONITOR_MATCH = 0x10000000,     // True if we know that monitorenter/monitorexit bytecodes match
            JVM_ACC_HAS_MONITOR_BYTECODES = 0x20000000,     // Method contains monitorenter/monitorexit bytecodes
            JVM_ACC_HAS_LOOPS = 0x40000000,     // Method has loops
            JVM_ACC_LOOPS_FLAG_INIT = 0x80000000,// The loop flag has been initialized
            JVM_ACC_QUEUED = 0x01000000,     // Queued for compilation
            JVM_ACC_NOT_C2_COMPILABLE = 0x02000000,
            JVM_ACC_NOT_C1_COMPILABLE = 0x04000000,
            JVM_ACC_NOT_C2_OSR_COMPILABLE = 0x08000000,
            JVM_ACC_HAS_LINE_NUMBER_TABLE = 0x00100000,
            JVM_ACC_HAS_CHECKED_EXCEPTIONS = 0x00400000,
            JVM_ACC_HAS_JSRS = 0x00800000,
            JVM_ACC_IS_OLD = 0x00010000,     // RedefineClasses() has replaced this method
            JVM_ACC_IS_OBSOLETE = 0x00020000,     // RedefineClasses() has made method obsolete
            JVM_ACC_IS_PREFIXED_NATIVE = 0x00040000,     // JVMTI has prefixed this native method
            JVM_ACC_ON_STACK = 0x00080000,     // RedefineClasses() was used on the stack
            JVM_ACC_IS_DELETED = 0x00008000,     // RedefineClasses() has deleted this method

    // Klass* flags
    JVM_ACC_HAS_MIRANDA_METHODS = 0x10000000,     // True if this class has miranda methods in it's vtable
            JVM_ACC_HAS_VANILLA_CONSTRUCTOR = 0x20000000,     // True if klass has a vanilla default constructor
            JVM_ACC_HAS_FINALIZER = 0x40000000,     // True if klass has a non-empty finalize() method
            JVM_ACC_IS_CLONEABLE = 0x80000000,// True if klass supports the Clonable interface
            JVM_ACC_HAS_FINAL_METHOD = 0x01000000,     // True if klass has final method

    // Klass* and Method* flags
    JVM_ACC_HAS_LOCAL_VARIABLE_TABLE = 0x00200000,

    JVM_ACC_PROMOTED_FLAGS = 0x00200000,     // flags promoted from methods to the holding klass

    // field flags
    // Note: these flags must be defined in the low order 16 bits because
    // InstanceKlass only stores a ushort worth of information from the
    // AccessFlags value.
    // These bits must not conflict with any other field-related access flags
    // (e.g., ACC_ENUM).
    // Note that the class-related ACC_ANNOTATION bit conflicts with these flags.
    JVM_ACC_FIELD_ACCESS_WATCHED = 0x00002000, // field access is watched by JVMTI
            JVM_ACC_FIELD_MODIFICATION_WATCHED = 0x00008000, // field modification is watched by JVMTI
            JVM_ACC_FIELD_INTERNAL = 0x00000400, // internal field, same as JVM_ACC_ABSTRACT
            JVM_ACC_FIELD_STABLE = 0x00000020, // @Stable field, same as JVM_ACC_SYNCHRONIZED and JVM_ACC_SUPER
            JVM_ACC_FIELD_INITIALIZED_FINAL_UPDATE = 0x00000100, // (static) final field updated outside (class) initializer, same as JVM_ACC_NATIVE
            JVM_ACC_FIELD_HAS_GENERIC_SIGNATURE = 0x00000800, // field has generic signature

    JVM_ACC_FIELD_INTERNAL_FLAGS = JVM_ACC_FIELD_ACCESS_WATCHED |
            JVM_ACC_FIELD_MODIFICATION_WATCHED |
            JVM_ACC_FIELD_INTERNAL |
            JVM_ACC_FIELD_STABLE |
            JVM_ACC_FIELD_HAS_GENERIC_SIGNATURE,

    // flags accepted by set_field_flags()
    JVM_ACC_FIELD_FLAGS = Math.toIntExact(JVM_RECOGNIZED_FIELD_MODIFIERS | JVM_ACC_FIELD_INTERNAL_FLAGS);

}
