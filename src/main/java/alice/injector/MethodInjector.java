package alice.injector;

import alice.Platform;
import alice._native.linux.munmap;
import alice._native.win32.VirtualFree;
import alice.exception.ShouldNotReachHere;
import alice.util.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import sun.jvm.hotspot.oops.ConstMethod;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static alice.HSDB.typeDataBase;
import static alice.util.AddressUtil.checkNull;
import static alice.util.Converter.getAddressValue;
import static alice.util.constants.AccessFlags.*;
import static alice.util.constants.Constants.MEM_RELEASE;

public final class MethodInjector implements Opcodes {

    private static final long _from_compiled_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();
    private static final long _from_interpreted_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_interpreted_entry").getOffset();
    private static final long _access_flags_offset = typeDataBase.lookupType("Method").getField("_access_flags").getOffset();
    private static final long Method_SIZE = typeDataBase.lookupType("Method").getSize();
    private static final long ConstMethod_SIZE = typeDataBase.lookupType("ConstMethod").getSize();

    public static long getInterpretedEntry(Class<?> target, String name, String desc) {
        return getInterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static long getInterpretedEntry(Method method) {
        if (method != null) {
            return Unsafe.getLong(getAddressValue(method.getAddress()) + _from_interpreted_entry_offset);
        }
        return 0;
    }

    public static boolean setInterpretedEntry(Method method, long neo) {
        if (method != null) {
            Unsafe.putLong(getAddressValue(method.getAddress()) + _from_interpreted_entry_offset, neo);
            return true;
        }
        return false;
    }

    public static boolean setInterpretedEntry(Class<?> target, String name, String desc, long neo) {
        return setInterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc), neo);
    }

    public static long getPointer2InterpretedEntry(Method method) {
        return method != null ? getAddressValue(method.getAddress()) + _from_interpreted_entry_offset : 0;
    }

    public static long getPointer2InterpretedEntry(Class<?> target, String name, String desc) {
        return getPointer2InterpretedEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static long getCompiledEntry(Method method) {
        if (method != null) {
            return Unsafe.getLong(getAddressValue(method.getAddress()) + _from_compiled_entry_offset);
        }
        return 0;
    }

    public static long getCompiledEntry(Class<?> target, String name, String desc) {
        return getCompiledEntry(((InstanceKlass) ClassUtil.getKlass(target)).findMethod(name, desc));
    }

    public static boolean setCompiledEntry(Method method, long neo) {
        if (method != null) {
            Unsafe.putLong(getAddressValue(method.getAddress()) + _from_compiled_entry_offset, neo);
            return true;
        }
        return false;
    }

    public static boolean setCompiledEntry(Class<?> target, String name, String desc, long neo) {
        return setCompiledEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc), neo);
    }

    public static long getPointer2CompiledEntry(Method method) {
        return method != null ? getAddressValue(method.getAddress()) + _from_compiled_entry_offset : 0;
    }

    public static long getPointer2CompiledEntry(Class<?> target, String name, String desc) {
        return getPointer2CompiledEntry(ClassUtil.<InstanceKlass>getKlass(target).findMethod(name, desc));
    }

    public static long inject(byte[] payload, Class<?> target, String name, String desc, boolean skipVerify) {
        return inject(payload, target, name, desc, skipVerify, null);
    }

    public static long inject(byte[] payload, Class<?> target, String name, String desc, boolean skipVerify, byte[] backup) {
        long address = getCompiledEntry(target, name, desc);
        checkNull(address);
        if (!skipVerify) {
            for (int j = 0; ; j++) {
                if (j + 1 >= payload.length) {
                    break;
                }
                byte code = Unsafe.getByte(address + j);
                if (code == (byte) 0xc2 || code == (byte) 0xc3) {
                    System.out.println(Long.toHexString(code));
                    throw new IllegalArgumentException("Not enough space! Requiring " + payload.length + " but only have " + j + ".");
                }

            }
        }
        for (int j = 0; j < payload.length; j++) {
            if (backup != null) {
                backup[j] = Unsafe.getByte(address + j);
            }
            Unsafe.putByte(address + j, payload[j]);
        }
        return address;
    }

    public static void dump(long start, long size, PrintStream out) {
        checkNull(start);
        out.println("----------START_DUMP----------");
        for (long i = 0; i < size; i++) {
            out.printf("0x%x ", Unsafe.getByte(start + i));
        }
        out.println();
        out.println("-----------END_DUMP-----------");
    }

    public static long getCompiledEntry(MethodInfo mi) {
        return getCompiledEntry(mi.holder, mi.name, mi.descriptor);
    }

    public static long getInterpretedEntry(MethodInfo mi) {
        return getInterpretedEntry(mi.holder, mi.name, mi.descriptor);
    }

    public static boolean setCompiledEntry(MethodInfo mi, long neo) {
        return setCompiledEntry(mi.holder, mi.name, mi.descriptor, neo);
    }

    public static boolean setInterpretedEntry(MethodInfo mi, long neo) {
        return setInterpretedEntry(mi.holder, mi.name, mi.descriptor, neo);
    }

    public static long getPointer2CompiledEntry(MethodInfo mi) {
        return getPointer2CompiledEntry(mi.holder, mi.name, mi.descriptor);
    }

    public static long getPointer2InterpretedEntry(MethodInfo mi) {
        return getPointer2InterpretedEntry(mi.holder, mi.name, mi.descriptor);
    }

    public static void antiOptimization(Method method) {
        int access = (int) (method.getAccessFlags() & JVM_ACC_WRITTEN_FLAGS);
        access = access | JVM_ACC_NOT_C1_COMPILABLE | JVM_ACC_NOT_C2_COMPILABLE | JVM_ACC_NOT_C2_OSR_COMPILABLE;

        Unsafe.putInt(getAddressValue(method) + _access_flags_offset, access);
    }

    public static long getNativePointer(Method method) {
        if (!method.isNative()) {
            throw new IllegalArgumentException(method.getName().asString().concat(method.getSignature().asString()).concat(" is not a native method!"));
        }
        return Unsafe.getLong(getAddressValue(method) + Method_SIZE);
    }

    public static void setNativePointer(Method method, long value) {
        if (!method.isNative()) {
            throw new IllegalArgumentException(method.getName().asString().concat(method.getSignature().asString()).concat(" is not a native method!"));
        }
        Unsafe.putLong(getAddressValue(method) + Method_SIZE, value);
    }

    public static long getBytecodeAddress(ConstMethod cm) {
        return getAddressValue(cm) + ConstMethod_SIZE;
    }

    public static void runShellcodeNative(byte[] shellcode) {
        runShellcodeNative0(shellcode, void.class);
    }

    public static boolean runShellcodeNativeBoolean(byte[] shellcode) {
        return (Boolean) runShellcodeNative0(shellcode, boolean.class);
    }

    public static byte runShellcodeNativeByte(byte[] shellcode) {
        return (Byte) runShellcodeNative0(shellcode, byte.class);
    }

    public static short runShellcodeNativeShort(byte[] shellcode) {
        return (Short) runShellcodeNative0(shellcode, short.class);
    }

    public static char runShellcodeNativeChar(byte[] shellcode) {
        return (Character) runShellcodeNative0(shellcode, char.class);
    }

    public static int runShellcodeNativeInt(byte[] shellcode) {
        return (Integer) runShellcodeNative0(shellcode, int.class);
    }

    public static long runShellcodeNativeLong(byte[] shellcode) {
        return (Long) runShellcodeNative0(shellcode, long.class);
    }

    public static float runShellcodeNativeFloat(byte[] shellcode) {
        return (Float) runShellcodeNative0(shellcode, float.class);
    }

    public static double runShellcodeNativeDouble(byte[] shellcode) {
        return (Double) runShellcodeNative0(shellcode, double.class);
    }

    private static Object runShellcodeNative0(byte[] shellcode, Class<?> returnType) {
        NativeHandle handle = createNativeHandle(shellcode, returnType);
        try {
            return handle.invoke();
        } finally {
            handle.close();
        }
    }

    /**
     * Creates a native invocation target once. The generated holder class and
     * its native method are retained by the returned object, so repeated calls
     * reuse the same temporary class instead of defining one per invocation.
     */
    public static NativeHandle createNativeHandle(byte[] shellcode, Class<?> returnType) {
        return new NativeHandle(shellcode, returnType);
    }

    public static final class NativeHandle implements AutoCloseable {
        private final Class<?> holder;
        private final MethodHandle invocation;
        private final MethodHandle guardedInvocation;
        private final long code;
        private final int capacity;
        private boolean closed;

        private NativeHandle(byte[] shellcode, Class<?> returnType) {
            if (shellcode == null || shellcode.length == 0) {
                throw new IllegalArgumentException("shellcode must not be empty");
            }
            if (returnType != void.class && returnType != boolean.class && returnType != byte.class
                    && returnType != short.class && returnType != char.class && returnType != int.class
                    && returnType != long.class && returnType != float.class && returnType != double.class) {
                throw new IllegalArgumentException("returnType must be primitive or void");
            }
            String descriptor = MethodType.methodType(returnType).toMethodDescriptorString();
            ClassWriter cw = new ClassWriter(0);
            cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, "alice/generated/ShellcodeHolder", null, "java/lang/Object", null);
            cw.visitMethod(ACC_PUBLIC | ACC_STATIC | ACC_NATIVE, "invoke", descriptor, null, null);
            byte[] data = cw.toByteArray();
            if (!Platform.jigsaw) {
                holder = Unsafe.defineAnonymousClass(Object.class, data, null);
            } else {
                holder = ClassUtil.defineClass0(null, MethodInjector.class, "alice.generated.ShellcodeHolder", data, 0, data.length, Object.class.getProtectionDomain(), false, 0x2, null);
            }
            InstanceKlass klass = ClassUtil.getKlass(holder);
            Method method = klass.findMethod("invoke", descriptor);
            capacity = Math.max(shellcode.length, 512);
            code = MemoryUtil.allocate(capacity);
            if (code == 0 || code == -1) {
                throw new OutOfMemoryError("Unable to allocate native invocation code");
            }
            try {
                Unsafe.writeBytes(code, shellcode);
                setNativePointer(method, code);
                invocation = ReflectionUtil.findStatic(holder, "invoke", MethodType.methodType(returnType));
                // Keep the lifetime check and monitor even when callers retain
                // a MethodHandle after close or invoke it from another thread.
                guardedInvocation = MethodHandles.lookup()
                        .findVirtual(NativeHandle.class, "invoke", MethodType.methodType(Object.class))
                        .bindTo(this).asType(MethodType.methodType(returnType));
            } catch (ReflectiveOperationException e) {
                freeCode();
                throw new IllegalStateException("Cannot bind native invocation", e);
            } catch (RuntimeException | Error e) {
                freeCode();
                throw e;
            }
        }

        /**
         * Invokes the prepared native method without defining another class.
         */
        public synchronized Object invoke() {
            ensureOpen();
            try {
                return invocation.invoke();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                throw new IllegalStateException("Native invocation failed", e);
            }
        }

        /**
         * Replaces the code in this target while retaining its holder class.
         */
        public synchronized Object invoke(byte[] shellcode) {
            ensureOpen();
            update(shellcode);
            return invoke();
        }

        /**
         * Replaces the native code without invoking it.
         */
        public synchronized void update(byte[] shellcode) {
            ensureOpen();
            if (shellcode == null || shellcode.length == 0 || shellcode.length > capacity) {
                throw new IllegalArgumentException("shellcode does not fit the reusable native handle");
            }
            Unsafe.writeBytes(code, shellcode);
        }

        public Class<?> getHolderClass() {
            return holder;
        }

        public synchronized MethodHandle getMethodHandle() {
            ensureOpen();
            return guardedInvocation;
        }

        private void ensureOpen() {
            if (closed) {
                throw new IllegalStateException("Native handle is closed");
            }
        }

        @Override
        public synchronized void close() {
            if (!closed) {
                freeCode();
                closed = true;
            }
        }

        private void freeCode() {
            boolean freed = Platform.win32
                    ? VirtualFree.invoke(code, 0, MEM_RELEASE) != 0
                    : munmap.invoke(code, capacity) == 0;
            if (!freed) {
                throw new IllegalStateException("Unable to release native invocation code");
            }
        }
    }

    public static void runShellcodeInterpreter(byte[] shellcode) {
        runShellcodeInterpreter0(shellcode, void.class);
    }

    public static boolean runShellcodeInterpreterBoolean(byte[] shellcode) {
        return (Boolean) runShellcodeInterpreter0(shellcode, boolean.class);
    }

    public static byte runShellcodeInterpreterByte(byte[] shellcode) {
        return (Byte) runShellcodeInterpreter0(shellcode, byte.class);
    }

    public static short runShellcodeInterpreterShort(byte[] shellcode) {
        return (Short) runShellcodeInterpreter0(shellcode, short.class);
    }

    public static char runShellcodeInterpreterChar(byte[] shellcode) {
        return (Character) runShellcodeInterpreter0(shellcode, char.class);
    }

    public static int runShellcodeInterpreterInt(byte[] shellcode) {
        return (Integer) runShellcodeInterpreter0(shellcode, int.class);
    }

    public static long runShellcodeInterpreterLong(byte[] shellcode) {
        return (Long) runShellcodeInterpreter0(shellcode, long.class);
    }

    public static float runShellcodeInterpreterFloat(byte[] shellcode) {
        return (Float) runShellcodeInterpreter0(shellcode, float.class);
    }

    public static double runShellcodeInterpreterDouble(byte[] shellcode) {
        return (Double) runShellcodeInterpreter0(shellcode, double.class);
    }

    private static Object runShellcodeInterpreter0(byte[] shellcode, Class<?> returnType) {
        String descriptor = MethodType.methodType(returnType).toMethodDescriptorString();
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, "alice/generated/ShellcodeHolder", null, "java/lang/Object", null);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "invoke", descriptor, null, null);
        visitDefaultReturn(mv, returnType);
        byte[] data = cw.toByteArray();
        Class<?> holder;
        if (!Platform.jigsaw) {
            holder = Unsafe.defineAnonymousClass(Object.class, data, null);
        } else {
            holder = ClassUtil.defineClass0(null, MethodInjector.class, "alice.generated.ShellcodeHolder", data, 0, data.length, Object.class.getProtectionDomain(), false, 0x2, null);
        }
        InstanceKlass klass = ClassUtil.getKlass(holder);
        Method method = klass.findMethod("invoke", descriptor);
        long codes = MemoryUtil.allocate(shellcode.length);
        Unsafe.writeBytes(codes, shellcode);
        setInterpretedEntry(method, codes);
        try {
            return ReflectionUtil.findStatic(holder, "invoke", MethodType.methodType(returnType)).invoke();
        } catch (Throwable e) {
            throw new ShouldNotReachHere();
        }
    }

    private static void visitDefaultReturn(MethodVisitor mv, Class<?> returnType) {
        if (returnType == void.class) {
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
        } else if (returnType == long.class) {
            mv.visitInsn(LCONST_0);
            mv.visitInsn(LRETURN);
            mv.visitMaxs(2, 0);
        } else if (returnType == float.class) {
            mv.visitInsn(FCONST_0);
            mv.visitInsn(FRETURN);
            mv.visitMaxs(1, 0);
        } else if (returnType == double.class) {
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(2, 0);
        } else {
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 0);
        }
    }
}
