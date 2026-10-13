package alice._native;

import alice.Platform;
import alice.exception.NoSuchFunctionException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static alice._native.ASMUtil.Register.*;

public final class Bridge {

    private static final ASMUtil.Register[] SYSTEM_V_ARGUMENTS = {RDI, RSI, RDX, RCX, R8, R9};
    private static final ASMUtil.Register[] SYSTEM_V_SYSCALL_ARGUMENTS = {RDI, RSI, RDX, R10, R8, R9};
    private static final ASMUtil.Register[] WINDOWS_X64_ARGUMENTS = {RCX, RDX, R8, R9};
    private static final int WINDOWS_X64_SHADOW_SPACE = 0x20;
    // movStack uses a signed disp8 displacement.
    private static final int MAX_STACK_OFFSET = 0x7f;
    private static final int RSP_ADJUST_CHUNK = 0x78;

    private Bridge() {
    }

    /**
     * Creates a reusable handle for a native function. The supplied arguments
     * are bound to the handle and are used when one of the typed invoke
     * methods is called without arguments. Passing arguments to an invoke
     * method creates a temporary call with those arguments instead.
     */
    public static Handle createHandle(String name, Object... args) {
        return createHandle(resolve(name), args);
    }

    public static Handle createHandle(long function, Object... args) {
        rejectLegacyFloatingArguments(args);
        return new Handle(function, false, 0, inferArgumentTypes(args), null,
                createPayload(function, args), true);
    }

    /**
     * Creates a reusable function handle with an explicit native signature.
     * Inputs may be primitive types or {@code String.class}; strings are
     * encoded as temporary NUL-terminated C strings for each invocation.
     */
    public static Handle createHandle(String name, Class<?>[] argumentTypes, Class<?> returnType) {
        return createHandle(resolve(name), argumentTypes, returnType);
    }

    public static Handle createHandle(long function, Class<?>[] argumentTypes, Class<?> returnType) {
        validateSignature(argumentTypes, returnType);
        return new Handle(function, false, 0, argumentTypes, returnType,
                createPayload(function, placeholderArguments(argumentTypes)), false);
    }

    /**
     * Creates a reusable handle for a system call. Unlike a native function
     * handle, this stores an integer system-service number (SSN), including
     * zero, and emits the processor's {@code syscall} instruction. Supported on
     * Linux and Windows x86-64. Linux accepts at most six arguments; Windows
     * accepts at most fifteen with the current stack emitter. Results are raw
     * integer values (including negative Linux errno values), not libc results.
     * The caller supplies the SSN for the running OS version.
     */
    public static Handle createSyscallHandle(int ssn, Object... args) {
        rejectLegacyFloatingArguments(args);
        return new Handle(0, true, ssn, inferArgumentTypes(args), null,
                createSyscallPayload(ssn, args), true);
    }

    public static Handle createSyscallHandle(int ssn, Class<?>[] argumentTypes, Class<?> returnType) {
        validateSignature(argumentTypes, returnType);
        if (returnType == float.class || returnType == double.class) {
            throw new UnsupportedOperationException("Syscalls return integer values");
        }
        return new Handle(0, true, ssn, argumentTypes, returnType,
                createSyscallPayload(ssn, placeholderArguments(argumentTypes)), false);
    }

    /**
     * Alias for the declared syscall factory when the first argument is an SSN.
     */
    public static Handle createHandle(int ssn, Class<?>[] argumentTypes, Class<?> returnType) {
        return createSyscallHandle(ssn, argumentTypes, returnType);
    }

    public static void invokeVoid(String name, Object... args) {
        invokeVoid(resolve(name), args);
    }

    public static void invokeVoid(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            MethodInjector.runShellcodeNative(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static boolean invokeBoolean(String name, Object... args) {
        return invokeBoolean(resolve(name), args);
    }

    public static boolean invokeBoolean(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeBoolean(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static byte invokeByte(String name, Object... args) {
        return invokeByte(resolve(name), args);
    }

    public static byte invokeByte(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeByte(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static short invokeShort(String name, Object... args) {
        return invokeShort(resolve(name), args);
    }

    public static short invokeShort(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeShort(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static char invokeChar(String name, Object... args) {
        return invokeChar(resolve(name), args);
    }

    public static char invokeChar(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeChar(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static int invokeInt(String name, Object... args) {
        return invokeInt(resolve(name), args);
    }

    public static int invokeInt(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeInt(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static long invokeLong(String name, Object... args) {
        return invokeLong(resolve(name), args);
    }

    public static long invokeLong(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeLong(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static float invokeFloat(String name, Object... args) {
        return invokeFloat(resolve(name), args);
    }

    public static float invokeFloat(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeFloat(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static double invokeDouble(String name, Object... args) {
        return invokeDouble(resolve(name), args);
    }

    public static double invokeDouble(long function, Object... args) {
        Payload payload = createPayload(function, args);
        try {
            return MethodInjector.runShellcodeNativeDouble(payload.bytes);
        } finally {
            payload.release();
        }
    }

    public static void invokeSyscallVoid(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            handle.invokeVoid();
        }
    }

    public static boolean invokeSyscallBoolean(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeBoolean();
        }
    }

    public static byte invokeSyscallByte(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeByte();
        }
    }

    public static short invokeSyscallShort(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeShort();
        }
    }

    public static char invokeSyscallChar(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeChar();
        }
    }

    public static int invokeSyscallInt(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeInt();
        }
    }

    public static long invokeSyscallLong(int ssn, Object... args) {
        try (Handle handle = createSyscallHandle(ssn, args)) {
            return handle.invokeLong();
        }
    }

    private static Payload createPayload(long function, Object... args) {
        if (function == 0) {
            throw new IllegalArgumentException("function must not be null");
        }
        ByteArrayList payload = new ByteArrayList(512);
        List<CString> temporaryStrings = new ArrayList<>();
        try {
            long[] values = convertArguments(args, temporaryStrings);
            if (Platform.win32) {
                emitWindowsX64Call(payload, function, values);
            } else {
                emitSystemVCall(payload, function, values);
            }
            return new Payload(payload.toByteArray(), temporaryStrings, args);
        } catch (RuntimeException | Error e) {
            releaseStrings(temporaryStrings);
            throw e;
        }
    }

    private static Payload createSyscallPayload(int ssn, Object... args) {
        if (!Platform.amd64 || (!Platform.linux && !Platform.win32)) {
            throw new UnsupportedOperationException("Syscalls require Linux or Windows x86-64");
        }
        if (ssn < 0) {
            throw new IllegalArgumentException("ssn must not be negative");
        }
        ByteArrayList payload = new ByteArrayList(512);
        List<CString> temporaryStrings = new ArrayList<>();
        try {
            long[] values = convertArguments(args, temporaryStrings);
            if (Platform.win32) {
                emitWindowsX64Syscall(payload, ssn, values);
            } else {
                emitSystemVSyscall(payload, ssn, values);
            }
            return new Payload(payload.toByteArray(), temporaryStrings, args);
        } catch (RuntimeException | Error e) {
            releaseStrings(temporaryStrings);
            throw e;
        }
    }

    private static void releaseStrings(List<CString> strings) {
        for (CString string : strings) {
            string.release();
        }
    }

    private static long resolve(String name) {
        long function = SymbolLookup.lookupValue(name);
        if (function == 0) {
            throw new NoSuchFunctionException(name);
        }
        return function;
    }

    private static void emitSystemVCall(ByteArrayList payload, long function, long[] args) {
        int registerCount = Math.min(args.length, SYSTEM_V_ARGUMENTS.length);
        int stackCount = args.length - registerCount;
        int stackBytes = stackCount * Long.BYTES;
        int stackPadding = (stackBytes & 0xf) == 0 ? 8 : 0;
        int stackAdjust = stackBytes + stackPadding;
        checkStackOffsets(stackCount, 0);

        subRsp(payload, stackAdjust);
        for (int i = 0; i < stackCount; i++) {
            ASMUtil.movabs(payload, RAX, args[registerCount + i]);
            ASMUtil.movStack(payload, i * Long.BYTES, RAX);
        }
        for (int i = 0; i < registerCount; i++) {
            ASMUtil.movabs(payload, SYSTEM_V_ARGUMENTS[i], args[i]);
        }
        ASMUtil.movabs(payload, RAX, function);
        ASMUtil.call(payload, RAX);
        addRsp(payload, stackAdjust);
        ASMUtil.ret(payload);
    }

    private static void emitWindowsX64Call(ByteArrayList payload, long function, long[] args) {
        int registerCount = Math.min(args.length, WINDOWS_X64_ARGUMENTS.length);
        int stackCount = args.length - registerCount;
        int stackBytes = stackCount * Long.BYTES;
        int stackAdjust = WINDOWS_X64_SHADOW_SPACE + stackBytes;
        if ((stackAdjust & 0xf) != 8) {
            stackAdjust += 8;
        }
        checkStackOffsets(stackCount, WINDOWS_X64_SHADOW_SPACE);

        subRsp(payload, stackAdjust);
        for (int i = 0; i < stackCount; i++) {
            ASMUtil.movabs(payload, RAX, args[registerCount + i]);
            ASMUtil.movStack(payload, WINDOWS_X64_SHADOW_SPACE + i * Long.BYTES, RAX);
        }
        for (int i = 0; i < registerCount; i++) {
            ASMUtil.movabs(payload, WINDOWS_X64_ARGUMENTS[i], args[i]);
        }
        ASMUtil.movabs(payload, RAX, function);
        ASMUtil.call(payload, RAX);
        addRsp(payload, stackAdjust);
        ASMUtil.ret(payload);
    }

    private static void emitSystemVSyscall(ByteArrayList payload, int ssn, long[] args) {
        if (args.length > SYSTEM_V_SYSCALL_ARGUMENTS.length) {
            throw new IllegalArgumentException("Too many arguments for a System V syscall");
        }
        for (int i = 0; i < args.length; i++) {
            ASMUtil.movabs(payload, SYSTEM_V_SYSCALL_ARGUMENTS[i], args[i]);
        }
        ASMUtil.movImm32(payload, RAX, ssn);
        ASMUtil.syscall(payload);
        ASMUtil.ret(payload);
    }

    private static void emitWindowsX64Syscall(ByteArrayList payload, int ssn, long[] args) {
        int registerCount = Math.min(args.length, WINDOWS_X64_ARGUMENTS.length);
        int stackCount = args.length - registerCount;
        // The kernel expects argument five at RSP + 0x28: a return slot plus
        // four home slots. Unlike a call, syscall does not push a return address.
        int firstStackOffset = WINDOWS_X64_SHADOW_SPACE + Long.BYTES;
        checkStackOffsets(stackCount, firstStackOffset);
        int stackAdjust = firstStackOffset + stackCount * Long.BYTES;
        stackAdjust = (stackAdjust + 0xf) & ~0xf;

        subRsp(payload, stackAdjust);
        for (int i = 0; i < stackCount; i++) {
            ASMUtil.movabs(payload, RAX, args[registerCount + i]);
            ASMUtil.movStack(payload, firstStackOffset + i * Long.BYTES, RAX);
        }
        for (int i = 0; i < registerCount; i++) {
            ASMUtil.movabs(payload, WINDOWS_X64_ARGUMENTS[i], args[i]);
        }
        ASMUtil.mov(payload, R10, RCX);
        ASMUtil.movImm32(payload, RAX, ssn);
        ASMUtil.syscall(payload);
        addRsp(payload, stackAdjust);
        ASMUtil.ret(payload);
    }

    private static long[] convertArguments(Object[] args, List<CString> temporaryStrings) {
        if (args == null || args.length == 0) {
            return new long[0];
        }
        long[] values = new long[args.length];
        for (int i = 0; i < args.length; i++) {
            values[i] = convertArgument(args[i], temporaryStrings);
        }
        return values;
    }

    private static long convertArgument(Object arg, List<CString> temporaryStrings) {
        if (arg == null) {
            return 0;
        }
        if (arg instanceof NativeObject) {
            return ((NativeObject) arg).address;
        }
        if (arg instanceof String) {
            CString cString = CString.create((String) arg);
            temporaryStrings.add(cString);
            return cString.address;
        }
        if (arg instanceof Boolean) {
            return (Boolean) arg ? 1 : 0;
        }
        if (arg instanceof Character) {
            return (Character) arg;
        }
        if (arg instanceof Float || arg instanceof Double) {
            throw new IllegalArgumentException("Floating-point arguments are not supported by Bridge");
        }
        if (arg instanceof Number) {
            return ((Number) arg).longValue();
        }
        throw new IllegalArgumentException("Unsupported native argument type: " + arg.getClass().getName());
    }

    private static void checkStackOffsets(int stackCount, int firstOffset) {
        if (stackCount == 0) {
            return;
        }
        int lastOffset = firstOffset + (stackCount - 1) * Long.BYTES;
        if (lastOffset > MAX_STACK_OFFSET) {
            throw new IllegalArgumentException("Too many stack arguments for the current bridge emitter");
        }
    }

    private static void subRsp(ByteArrayList payload, int bytes) {
        while (bytes > 0) {
            int chunk = Math.min(bytes, RSP_ADJUST_CHUNK);
            ASMUtil.subRsp(payload, chunk);
            bytes -= chunk;
        }
    }

    private static void addRsp(ByteArrayList payload, int bytes) {
        while (bytes > 0) {
            int chunk = Math.min(bytes, RSP_ADJUST_CHUNK);
            ASMUtil.addRsp(payload, chunk);
            bytes -= chunk;
        }
    }

    private static Class<?>[] inferArgumentTypes(Object[] args) {
        if (args == null || args.length == 0) {
            return new Class<?>[0];
        }
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null || arg instanceof NativeObject) {
                types[i] = long.class;
            } else if (arg instanceof String) {
                types[i] = String.class;
            } else if (arg instanceof Boolean) {
                types[i] = boolean.class;
            } else if (arg instanceof Character) {
                types[i] = char.class;
            } else if (arg instanceof Byte) {
                types[i] = byte.class;
            } else if (arg instanceof Short) {
                types[i] = short.class;
            } else if (arg instanceof Integer) {
                types[i] = int.class;
            } else if (arg instanceof Long) {
                types[i] = long.class;
            } else if (arg instanceof Float) {
                types[i] = float.class;
            } else if (arg instanceof Double) {
                types[i] = double.class;
            } else {
                types[i] = arg.getClass();
            }
        }
        return types;
    }

    private static void validateSignature(Class<?>[] argumentTypes, Class<?> returnType) {
        if (argumentTypes == null) {
            throw new IllegalArgumentException("argumentTypes must not be null");
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            Class<?> type = argumentTypes[i];
            if (type == null) {
                throw new IllegalArgumentException("argumentTypes[" + i + "] must not be null");
            }
            if (type != String.class && (!isPrimitive(type) || type == void.class)) {
                throw new IllegalArgumentException("Argument type must be primitive or String.class: " + type.getName());
            }
        }
        if (returnType == null || !isPrimitive(returnType)) {
            throw new IllegalArgumentException("returnType must be primitive or void");
        }
    }

    private static Object[] placeholderArguments(Class<?>[] argumentTypes) {
        Object[] values = new Object[argumentTypes.length];
        for (int i = 0; i < argumentTypes.length; i++) {
            Class<?> type = argumentTypes[i];
            if (type == String.class) {
                // A null C-string pointer keeps the reusable placeholder free
                // of temporary native allocations. Real strings are allocated
                // for each declared invocation and released afterwards.
                values[i] = null;
            } else if (type == boolean.class) {
                values[i] = false;
            } else if (type == char.class) {
                values[i] = '\0';
            } else if (type == byte.class) {
                values[i] = (byte) 0;
            } else if (type == short.class) {
                values[i] = (short) 0;
            } else if (type == int.class) {
                values[i] = 0;
            } else if (type == long.class) {
                values[i] = 0L;
            } else if (type == float.class) {
                values[i] = 0L;
            } else if (type == double.class) {
                values[i] = 0L;
            }
        }
        return values;
    }

    private static void validateDeclaredArguments(Class<?>[] argumentTypes, Object[] args) {
        int actualCount = args == null ? 0 : args.length;
        if (actualCount != argumentTypes.length) {
            throw new IllegalArgumentException("Expected " + argumentTypes.length
                    + " arguments but received " + actualCount);
        }
        for (int i = 0; i < argumentTypes.length; i++) {
            Class<?> expected = argumentTypes[i];
            Object actual = args[i];
            if (actual == null) {
                if (expected == String.class) {
                    continue;
                }
                throw new IllegalArgumentException("Argument " + i + " must be " + expected.getName());
            }
            if (expected == String.class) {
                if (!(actual instanceof String)) {
                    throw new IllegalArgumentException("Argument " + i + " must be a String");
                }
            } else if (expected == boolean.class && !(actual instanceof Boolean)) {
                throw new IllegalArgumentException("Argument " + i + " must be boolean");
            } else if (expected == char.class && !(actual instanceof Character)) {
                throw new IllegalArgumentException("Argument " + i + " must be char");
            } else if (expected == byte.class && !(actual instanceof Byte)) {
                throw new IllegalArgumentException("Argument " + i + " must be byte");
            } else if (expected == short.class && !(actual instanceof Short)) {
                throw new IllegalArgumentException("Argument " + i + " must be short");
            } else if (expected == int.class && !(actual instanceof Integer)) {
                throw new IllegalArgumentException("Argument " + i + " must be int");
            } else if (expected == long.class
                    && !(actual instanceof Long) && !(actual instanceof NativeObject)) {
                throw new IllegalArgumentException("Argument " + i + " must be long");
            } else if (expected == float.class && !(actual instanceof Float)) {
                throw new IllegalArgumentException("Argument " + i + " must be float");
            } else if (expected == double.class && !(actual instanceof Double)) {
                throw new IllegalArgumentException("Argument " + i + " must be double");
            }
        }
    }

    private static void rejectLegacyFloatingArguments(Object[] args) {
        if (args == null) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof Float || arg instanceof Double) {
                throw new IllegalArgumentException("Floating-point arguments are not supported by legacy Bridge calls");
            }
        }
    }

    private static void validateReturnType(Class<?> returnType) {
        if (returnType == null || !isPrimitive(returnType)) {
            throw new IllegalArgumentException("returnType must be primitive or void");
        }
    }

    private static boolean isPrimitive(Class<?> type) {
        return type == boolean.class || type == byte.class || type == short.class
                || type == char.class || type == int.class || type == long.class
                || type == float.class || type == double.class || type == void.class;
    }

    private static final class Payload {
        private final byte[] bytes;
        private final List<CString> temporaryStrings;
        // Keep caller-owned NativeObjects reachable while their addresses are used.
        private Object[] arguments;
        private boolean released;

        private Payload(byte[] bytes, List<CString> temporaryStrings, Object[] arguments) {
            this.bytes = bytes;
            this.temporaryStrings = temporaryStrings;
            this.arguments = arguments == null ? null : arguments.clone();
        }

        private synchronized void release() {
            if (released) {
                return;
            }
            releaseStrings(temporaryStrings);
            temporaryStrings.clear();
            arguments = null;
            released = true;
        }
    }

    /**
     * A reusable prepared call. Each return type gets one generated native
     * holder, MethodHandle and code allocation, retained until close. Calls
     * with new arguments update that allocation without generating a class.
     * Invocation and close are serialized on this handle.
     * Caller-owned NativeObjects must remain unreleased until the call finishes.
     */
    public static final class Handle implements AutoCloseable {
        private final long function;
        private final boolean syscall;
        private final int ssn;
        private final Class<?>[] argumentTypes;
        private final Class<?> returnType;
        private final boolean bound;
        private final Payload payload;
        private final Map<Class<?>, MethodInjector.NativeHandle> nativeHandles = new HashMap<>();
        private final Map<Class<?>, MethodHandle> methodHandles = new HashMap<>();
        private MethodHandle declaredMethodHandle;
        private boolean closed;

        private Handle(long function, boolean syscall, int ssn, Class<?>[] argumentTypes,
                       Class<?> returnType, Payload payload, boolean bound) {
            this.function = function;
            this.syscall = syscall;
            this.ssn = ssn;
            this.argumentTypes = argumentTypes.clone();
            this.returnType = returnType;
            this.payload = payload;
            this.bound = bound;
        }

        public boolean isSyscall() {
            return syscall;
        }

        public long getFunction() {
            if (syscall) {
                throw new IllegalStateException("A syscall handle has no function address");
            }
            return function;
        }

        public int getSsn() {
            if (!syscall) {
                throw new IllegalStateException("A native function handle has no SSN");
            }
            return ssn;
        }

        public synchronized void invokeVoid(Object... args) {
            checkReturnType(void.class);
            Payload call = payloadFor(args);
            try {
                invoke(call, void.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized boolean invokeBoolean(Object... args) {
            checkReturnType(boolean.class);
            Payload call = payloadFor(args);
            try {
                return (Boolean) invoke(call, boolean.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized byte invokeByte(Object... args) {
            checkReturnType(byte.class);
            Payload call = payloadFor(args);
            try {
                return (Byte) invoke(call, byte.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized short invokeShort(Object... args) {
            checkReturnType(short.class);
            Payload call = payloadFor(args);
            try {
                return (Short) invoke(call, short.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized char invokeChar(Object... args) {
            checkReturnType(char.class);
            Payload call = payloadFor(args);
            try {
                return (Character) invoke(call, char.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized int invokeInt(Object... args) {
            checkReturnType(int.class);
            Payload call = payloadFor(args);
            try {
                return (Integer) invoke(call, int.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized long invokeLong(Object... args) {
            checkReturnType(long.class);
            Payload call = payloadFor(args);
            try {
                return (Long) invoke(call, long.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized float invokeFloat(Object... args) {
            if (syscall) {
                throw new UnsupportedOperationException("Syscalls return integer values");
            }
            checkReturnType(float.class);
            Payload call = payloadFor(args);
            try {
                return (Float) invoke(call, float.class);
            } finally {
                releaseTemporary(call);
            }
        }

        public synchronized double invokeDouble(Object... args) {
            if (syscall) {
                throw new UnsupportedOperationException("Syscalls return integer values");
            }
            checkReturnType(double.class);
            Payload call = payloadFor(args);
            try {
                return (Double) invoke(call, double.class);
            } finally {
                releaseTemporary(call);
            }
        }

        private Payload payloadFor(Object[] args) {
            if (closed) {
                throw new IllegalStateException("Handle is closed");
            }
            if (!bound) {
                validateDeclaredArguments(argumentTypes, args);
                if (argumentTypes.length == 0) {
                    return payload;
                }
                return syscall ? createSyscallPayload(ssn, args) : createPayload(function, args);
            }
            if (args == null || args.length == 0) {
                return payload;
            }
            return syscall ? createSyscallPayload(ssn, args) : createPayload(function, args);
        }

        private void releaseTemporary(Payload call) {
            if (call != payload) {
                call.release();
            }
        }

        private Object invoke(Payload call, Class<?> returnType) {
            MethodInjector.NativeHandle nativeHandle = nativeHandleFor(call, returnType);
            return nativeHandle.invoke(call.bytes);
        }

        private void checkReturnType(Class<?> requested) {
            if (returnType != null && returnType != requested) {
                throw new IllegalStateException("Handle returns " + returnType.getName()
                        + ", not " + requested.getName());
            }
        }

        private MethodInjector.NativeHandle nativeHandleFor(Payload call, Class<?> returnType) {
            MethodInjector.NativeHandle nativeHandle = nativeHandles.get(returnType);
            if (nativeHandle == null) {
                nativeHandle = MethodInjector.createNativeHandle(call.bytes, returnType);
                nativeHandles.put(returnType, nativeHandle);
            }
            return nativeHandle;
        }

        /**
         * Returns the same zero-argument MethodHandle for each return type.
         * Obtaining it does not execute the native call. It always invokes the
         * bound arguments, even after calls with temporary arguments, and keeps
         * this handle alive. Calling it after close throws IllegalStateException.
         */
        public synchronized MethodHandle getMethodHandle(Class<?> returnType) {
            if (closed) {
                throw new IllegalStateException("Handle is closed");
            }
            if (!bound) {
                if (this.returnType != returnType) {
                    throw new IllegalArgumentException("The declared return type is "
                            + this.returnType.getName());
                }
                return getMethodHandle();
            }
            validateReturnType(returnType);
            if (returnType != void.class && returnType != boolean.class && returnType != byte.class
                    && returnType != short.class && returnType != char.class && returnType != int.class
                    && returnType != long.class && returnType != float.class && returnType != double.class) {
                throw new IllegalArgumentException("returnType must be primitive or void");
            }
            if (syscall && (returnType == float.class || returnType == double.class)) {
                throw new UnsupportedOperationException("Syscalls return integer values");
            }
            MethodHandle result = methodHandles.get(returnType);
            if (result == null) {
                String name = returnType.getName();
                name = "invoke" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                try {
                    result = MethodHandles.lookup()
                            .findVirtual(Handle.class, name, MethodType.methodType(returnType, Object[].class))
                            .bindTo(this).asFixedArity().asCollector(Object[].class, 0);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Cannot bind bridge invocation", e);
                }
                nativeHandleFor(payload, returnType);
                methodHandles.put(returnType, result);
            }
            return result;
        }

        /**
         * Returns a reusable MethodHandle with the signature declared when
         * this handle was created. Every invocation receives those arguments,
         * updates the retained native code, and then executes it.
         */
        public synchronized MethodHandle getMethodHandle() {
            if (closed) {
                throw new IllegalStateException("Handle is closed");
            }
            if (bound) {
                throw new IllegalStateException("A bound handle has no declared signature");
            }
            if (declaredMethodHandle == null) {
                nativeHandleFor(payload, returnType);
                try {
                    MethodHandle target = MethodHandles.lookup()
                            .findVirtual(Handle.class, "invokeDeclared",
                                    MethodType.methodType(Object.class, Object[].class))
                            .bindTo(this)
                            .asCollector(Object[].class, argumentTypes.length);
                    declaredMethodHandle = target.asType(MethodType.methodType(returnType, argumentTypes));
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Cannot bind declared bridge invocation", e);
                }
            }
            return declaredMethodHandle;
        }

        private synchronized Object invokeDeclared(Object[] args) {
            if (closed) {
                throw new IllegalStateException("Handle is closed");
            }
            Payload call = payloadFor(args);
            try {
                return invoke(call, returnType);
            } finally {
                releaseTemporary(call);
            }
        }

        @Override
        public synchronized void close() {
            if (!closed) {
                closed = true;
                try {
                    for (MethodInjector.NativeHandle nativeHandle : nativeHandles.values()) {
                        nativeHandle.close();
                    }
                } finally {
                    nativeHandles.clear();
                    methodHandles.clear();
                    declaredMethodHandle = null;
                    payload.release();
                }
            }
        }
    }

}
