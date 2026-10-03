package alice._native;

import alice.Platform;
import alice.exception.NoSuchFunctionException;
import alice.injector.MethodInjector;
import alice.injector.SymbolLookup;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.util.ArrayList;
import java.util.List;

import static alice._native.ASMUtil.Register.*;

public final class Bridge {

    private static final ASMUtil.Register[] SYSTEM_V_ARGUMENTS = {RDI, RSI, RDX, RCX, R8, R9};
    private static final ASMUtil.Register[] WINDOWS_X64_ARGUMENTS = {RCX, RDX, R8, R9};
    private static final int WINDOWS_X64_SHADOW_SPACE = 0x20;
    private static final int MAX_STACK_OFFSET = 0xff;
    private static final int RSP_ADJUST_CHUNK = 0x78;

    private Bridge() {
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

    private static Payload createPayload(long function, Object... args) {
        if (function == 0) {
            throw new IllegalArgumentException("function must not be null");
        }
        ByteArrayList payload = new ByteArrayList(512);
        List<CString> temporaryStrings = new ArrayList<>();
        long[] values = convertArguments(args, temporaryStrings);
        if (Platform.win32) {
            emitWindowsX64Call(payload, function, values);
        } else {
            emitSystemVCall(payload, function, values);
        }
        return new Payload(payload.toByteArray(), temporaryStrings);
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

    private static final class Payload {
        private final byte[] bytes;
        private final List<CString> temporaryStrings;

        private Payload(byte[] bytes, List<CString> temporaryStrings) {
            this.bytes = bytes;
            this.temporaryStrings = temporaryStrings;
        }

        private void release() {
            for (CString temporaryString : temporaryStrings) {
                temporaryString.release();
            }
        }
    }

}
