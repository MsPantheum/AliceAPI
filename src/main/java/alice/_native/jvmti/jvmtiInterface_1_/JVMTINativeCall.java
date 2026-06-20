package alice._native.jvmti.jvmtiInterface_1_;

import alice.Platform;
import alice._native.ASMUtil;
import alice.injector.MethodInjector;
import alice.util.*;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import java.util.HashMap;
import java.util.Map;

import static alice._native.ASMUtil.Register.*;

final class JVMTINativeCall {

    static final int INTEGER = 0;
    static final int FLOAT = 1;
    static final int DOUBLE = 2;

    private static final boolean SYSTEM_V = Platform.abi == Platform.ABI.SYSTEM_V;
    private static final Map<Long, long[]> ARG_OFFSETS = new HashMap<>();

    static final int SET_EVENT_NOTIFICATION_MODE = 1;
    static final int GET_ALL_THREADS = 3;
    static final int SUSPEND_THREAD = 4;
    static final int RESUME_THREAD = 5;
    static final int STOP_THREAD = 6;
    static final int INTERRUPT_THREAD = 7;
    static final int GET_THREAD_INFO = 8;
    static final int GET_OWNED_MONITOR_INFO = 9;
    static final int GET_CURRENT_CONTENDED_MONITOR = 10;
    static final int RUN_AGENT_THREAD = 11;
    static final int GET_TOP_THREAD_GROUPS = 12;
    static final int GET_THREAD_GROUP_INFO = 13;
    static final int GET_THREAD_GROUP_CHILDREN = 14;
    static final int GET_FRAME_COUNT = 15;
    static final int GET_THREAD_STATE = 16;
    static final int GET_CURRENT_THREAD = 17;
    static final int GET_FRAME_LOCATION = 18;
    static final int NOTIFY_FRAME_POP = 19;
    static final int GET_LOCAL_OBJECT = 20;
    static final int GET_LOCAL_INT = 21;
    static final int GET_LOCAL_LONG = 22;
    static final int GET_LOCAL_FLOAT = 23;
    static final int GET_LOCAL_DOUBLE = 24;
    static final int SET_LOCAL_OBJECT = 25;
    static final int SET_LOCAL_INT = 26;
    static final int SET_LOCAL_LONG = 27;
    static final int SET_LOCAL_FLOAT = 28;
    static final int SET_LOCAL_DOUBLE = 29;
    static final int CREATE_RAW_MONITOR = 30;
    static final int DESTROY_RAW_MONITOR = 31;
    static final int RAW_MONITOR_ENTER = 32;
    static final int RAW_MONITOR_EXIT = 33;
    static final int RAW_MONITOR_WAIT = 34;
    static final int RAW_MONITOR_NOTIFY = 35;
    static final int RAW_MONITOR_NOTIFY_ALL = 36;
    static final int SET_BREAKPOINT = 37;
    static final int CLEAR_BREAKPOINT = 38;
    static final int SET_FIELD_ACCESS_WATCH = 40;
    static final int CLEAR_FIELD_ACCESS_WATCH = 41;
    static final int SET_FIELD_MODIFICATION_WATCH = 42;
    static final int CLEAR_FIELD_MODIFICATION_WATCH = 43;
    static final int IS_MODIFIABLE_CLASS = 44;
    static final int ALLOCATE = 45;
    static final int DEALLOCATE = 46;
    static final int GET_CLASS_SIGNATURE = 47;
    static final int GET_CLASS_STATUS = 48;
    static final int GET_SOURCE_FILE_NAME = 49;
    static final int GET_CLASS_MODIFIERS = 50;
    static final int GET_CLASS_METHODS = 51;
    static final int GET_CLASS_FIELDS = 52;
    static final int GET_IMPLEMENTED_INTERFACES = 53;
    static final int IS_INTERFACE = 54;
    static final int IS_ARRAY_CLASS = 55;
    static final int GET_CLASS_LOADER = 56;
    static final int GET_OBJECT_HASH_CODE = 57;
    static final int GET_OBJECT_MONITOR_USAGE = 58;
    static final int GET_FIELD_NAME = 59;
    static final int GET_FIELD_DECLARING_CLASS = 60;
    static final int GET_FIELD_MODIFIERS = 61;
    static final int IS_FIELD_SYNTHETIC = 62;
    static final int GET_METHOD_NAME = 63;
    static final int GET_METHOD_DECLARING_CLASS = 64;
    static final int GET_METHOD_MODIFIERS = 65;
    static final int GET_MAX_LOCALS = 67;
    static final int GET_ARGUMENTS_SIZE = 68;
    static final int GET_LINE_NUMBER_TABLE = 69;
    static final int GET_METHOD_LOCATION = 70;
    static final int GET_LOCAL_VARIABLE_TABLE = 71;
    static final int SET_NATIVE_METHOD_PREFIX = 72;
    static final int SET_NATIVE_METHOD_PREFIXES = 73;
    static final int GET_BYTECODES = 74;
    static final int IS_METHOD_NATIVE = 75;
    static final int IS_METHOD_SYNTHETIC = 76;
    static final int GET_LOADED_CLASSES = 77;
    static final int GET_CLASS_LOADER_CLASSES = 78;
    static final int POP_FRAME = 79;
    static final int FORCE_EARLY_RETURN_OBJECT = 80;
    static final int FORCE_EARLY_RETURN_INT = 81;
    static final int FORCE_EARLY_RETURN_LONG = 82;
    static final int FORCE_EARLY_RETURN_FLOAT = 83;
    static final int FORCE_EARLY_RETURN_DOUBLE = 84;
    static final int FORCE_EARLY_RETURN_VOID = 85;
    static final int REDEFINE_CLASSES = 86;
    static final int GET_VERSION_NUMBER = 87;
    static final int GET_CAPABILITIES = 88;
    static final int GET_SOURCE_DEBUG_EXTENSION = 89;
    static final int IS_METHOD_OBSOLETE = 90;
    static final int SUSPEND_THREAD_LIST = 91;
    static final int RESUME_THREAD_LIST = 92;
    static final int GET_ALL_STACK_TRACES = 99;
    static final int GET_THREAD_LIST_STACK_TRACES = 100;
    static final int GET_THREAD_LOCAL_STORAGE = 101;
    static final int SET_THREAD_LOCAL_STORAGE = 102;
    static final int GET_STACK_TRACE = 103;
    static final int GET_TAG = 105;
    static final int SET_TAG = 106;
    static final int ITERATE_OVER_OBJECTS_REACHABLE_FROM_OBJECT = 108;
    static final int ITERATE_OVER_REACHABLE_OBJECTS = 109;
    static final int ITERATE_OVER_HEAP = 110;
    static final int ITERATE_OVER_INSTANCES_OF_CLASS = 111;
    static final int GET_OBJECTS_WITH_TAGS = 113;
    static final int FOLLOW_REFERENCES = 114;
    static final int ITERATE_THROUGH_HEAP = 115;
    static final int SET_JNI_FUNCTION_TABLE = 119;
    static final int GET_JNI_FUNCTION_TABLE = 120;
    static final int SET_EVENT_CALLBACKS = 121;
    static final int GENERATE_EVENTS = 122;
    static final int GET_EXTENSION_FUNCTIONS = 123;
    static final int GET_EXTENSION_EVENTS = 124;
    static final int SET_EXTENSION_EVENT_CALLBACK = 125;
    static final int GET_ERROR_NAME = 127;
    static final int GET_J_LOCATION_FORMAT = 128;
    static final int GET_SYSTEM_PROPERTIES = 129;
    static final int GET_SYSTEM_PROPERTY = 130;
    static final int SET_SYSTEM_PROPERTY = 131;
    static final int GET_PHASE = 132;
    static final int GET_CURRENT_THREAD_CPU_TIMER_INFO = 133;
    static final int GET_CURRENT_THREAD_CPU_TIME = 134;
    static final int GET_THREAD_CPU_TIMER_INFO = 135;
    static final int GET_THREAD_CPU_TIME = 136;
    static final int GET_TIMER_INFO = 137;
    static final int GET_TIME = 138;
    static final int GET_POTENTIAL_CAPABILITIES = 139;
    static final int ADD_CAPABILITIES = 141;
    static final int RELINQUISH_CAPABILITIES = 142;
    static final int GET_AVAILABLE_PROCESSORS = 143;
    static final int GET_CLASS_VERSION_NUMBERS = 144;
    static final int GET_CONSTANT_POOL = 145;
    static final int GET_ENVIRONMENT_LOCAL_STORAGE = 146;
    static final int SET_ENVIRONMENT_LOCAL_STORAGE = 147;
    static final int ADD_TO_BOOTSTRAP_CLASS_LOADER_SEARCH = 148;
    static final int SET_VERBOSE_FLAG = 149;
    static final int ADD_TO_SYSTEM_CLASS_LOADER_SEARCH = 150;
    static final int RETRANSFORM_CLASSES = 151;
    static final int GET_OWNED_MONITOR_STACK_DEPTH_INFO = 152;
    static final int GET_OBJECT_SIZE = 153;
    static final int GET_LOCAL_INSTANCE = 154;

    private JVMTINativeCall() {
    }

    static long create(Class<?> holderClass, String holderDescriptor, int functionIndex, int[] argumentTypes) {
        if (argumentTypes.length > 5) {
            throw new IllegalArgumentException("Unsupported JVMTI argument count: " + argumentTypes.length);
        }

        Payload payload = SYSTEM_V ? createSystemV(argumentTypes) : createWindowsX64(argumentTypes);
        long codeBase = MemoryUtil.allocate(payload.bytes.length);
        AddressUtil.checkNull(codeBase);
        Unsafe.writeBytes(codeBase, payload.bytes);
        ARG_OFFSETS.put(codeBase, payload.argumentOffsets);

        long env = JNIUtil.getJVMTIEnv();
        long function = function(env, functionIndex);
        Unsafe.putLong(codeBase + payload.functionOffset, function);

        InstanceKlass klass = ClassUtil.getKlass(holderClass);
        Method method = klass.findMethod("holder", holderDescriptor);
        MethodInjector.setNativePointer(method, codeBase);
        return codeBase;
    }

    static void setEnv(long codeBase, long env) {
        Unsafe.putLong(codeBase + envOffset(), env);
    }

    static void setArg(long codeBase, int index, long value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), value);
    }

    static void setArg(long codeBase, int index, int value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), value);
    }

    static void setArg(long codeBase, int index, boolean value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), value ? 1L : 0L);
    }

    static void setArg(long codeBase, int index, byte value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), value);
    }

    static void setArg(long codeBase, int index, float value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), Float.floatToRawIntBits(value) & 0xffffffffL);
    }

    static void setArg(long codeBase, int index, double value) {
        Unsafe.putLong(codeBase + argOffset(codeBase, index), Double.doubleToRawLongBits(value));
    }

    private static long function(long env, int index) {
        long functionTable = Unsafe.getLong(env);
        AddressUtil.checkNull(functionTable);
        long function = Unsafe.getLong(functionTable + (long) Unsafe.ADDRESS_SIZE * index);
        AddressUtil.checkNull(function);
        return function;
    }

    private static Payload createSystemV(int[] argumentTypes) {
        int size = 10;
        for (int argumentType : argumentTypes) {
            size += argumentType == INTEGER ? 10 : 15;
        }
        size += 12;
        byte[] payload = new byte[size];
        long[] argumentOffsets = new long[argumentTypes.length];
        int p = 0;
        int generalRegisterIndex = 0;
        int floatRegisterIndex = 0;
        ASMUtil.Register[] generalRegisters = {RSI, RDX, RCX, R8, R9};

        p = ASMUtil.movabs(payload, p, RDI); // jvmtiEnv*
        for (int i = 0; i < argumentTypes.length; i++) {
            argumentOffsets[i] = p + 2L;
            if (argumentTypes[i] == INTEGER) {
                p = ASMUtil.movabs(payload, p, generalRegisters[generalRegisterIndex++]);
            } else {
                p = ASMUtil.movabs(payload, p, RAX);
                p = movqXmmRax(payload, p, floatRegisterIndex++);
            }
        }
        long functionOffset = p + 2L;
        p = ASMUtil.movabs(payload, p, RAX); // function
        ASMUtil.jmp(payload, p, RAX);
        return new Payload(payload, argumentOffsets, functionOffset);
    }

    private static Payload createWindowsX64(int[] argumentTypes) {
        int size = 14;
        for (int i = 0; i < argumentTypes.length; i++) {
            if (i < 3) {
                size += argumentTypes[i] == INTEGER ? 10 : 15;
            } else {
                size += 15;
            }
        }
        size += 22;
        byte[] payload = new byte[size];
        long[] argumentOffsets = new long[argumentTypes.length];
        int p = 0;
        ASMUtil.Register[] generalRegisters = {RDX, R8, R9};

        p = ASMUtil.subRsp(payload, p, 0x38);
        p = ASMUtil.movabs(payload, p, RCX); // jvmtiEnv*
        for (int i = 0; i < argumentTypes.length; i++) {
            argumentOffsets[i] = p + 2L;
            if (i < 3) {
                if (argumentTypes[i] == INTEGER) {
                    p = ASMUtil.movabs(payload, p, generalRegisters[i]);
                } else {
                    p = ASMUtil.movabs(payload, p, RAX);
                    p = movqXmmRax(payload, p, i + 1);
                }
            } else {
                p = ASMUtil.movabs(payload, p, RAX);
                p = ASMUtil.movStack(payload, p, 0x20 + (i - 3) * 8, RAX);
            }
        }
        long functionOffset = p + 2L;
        p = ASMUtil.movabs(payload, p, RAX); // function
        p = ASMUtil.call(payload, p, RAX);
        p = ASMUtil.addRsp(payload, p, 0x38);
        ASMUtil.ret(payload, p);
        return new Payload(payload, argumentOffsets, functionOffset);
    }

    private static int movqXmmRax(byte[] payload, int index, int xmmRegister) {
        payload[index] = (byte) 0x66;
        payload[index + 1] = (byte) 0x48;
        payload[index + 2] = (byte) 0x0f;
        payload[index + 3] = (byte) 0x6e;
        payload[index + 4] = (byte) (0xc0 | ((xmmRegister & 7) << 3));
        return index + 5;
    }

    private static long envOffset() {
        return SYSTEM_V ? 2 : 6;
    }

    private static long argOffset(long codeBase, int index) {
        long[] argumentOffsets = ARG_OFFSETS.get(codeBase);
        if (argumentOffsets == null || index < 0 || index >= argumentOffsets.length) {
            throw new IllegalArgumentException("Unsupported JVMTI argument index: " + index);
        }
        return argumentOffsets[index];
    }

    private static final class Payload {
        private final byte[] bytes;
        private final long[] argumentOffsets;
        private final long functionOffset;

        private Payload(byte[] bytes, long[] argumentOffsets, long functionOffset) {
            this.bytes = bytes;
            this.argumentOffsets = argumentOffsets;
            this.functionOffset = functionOffset;
        }
    }
}
