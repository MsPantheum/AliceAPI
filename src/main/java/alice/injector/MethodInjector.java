package alice.injector;

import alice.Platform;
import alice.exception.ShouldNotReachHere;
import alice.util.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import sun.jvm.hotspot.oops.ConstMethod;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.oops.Method;

import java.io.PrintStream;
import java.lang.invoke.MethodType;

import static alice.HSDB.typeDataBase;
import static alice.util.AddressUtil.checkNull;
import static alice.util.Converter.getAddressValue;
import static alice.util.constants.AccessFlags.*;

public final class MethodInjector implements Opcodes {

    private static final long _from_compiled_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_compiled_entry").getOffset();
    private static final long _from_interpreted_entry_offset = typeDataBase.lookupType("Method").getAddressField("_from_interpreted_entry").getOffset();

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

    private static final long _access_flags_offset = typeDataBase.lookupType("Method").getField("_access_flags").getOffset();

    public static void antiOptimization(Method method) {
        int access = (int) (method.getAccessFlags() & JVM_ACC_WRITTEN_FLAGS);
        access = access | JVM_ACC_NOT_C1_COMPILABLE | JVM_ACC_NOT_C2_COMPILABLE | JVM_ACC_NOT_C2_OSR_COMPILABLE;

        Unsafe.putInt(getAddressValue(method) + _access_flags_offset, access);
    }

    private static final long Method_SIZE = typeDataBase.lookupType("Method").getSize();
    private static final long ConstMethod_SIZE = typeDataBase.lookupType("ConstMethod").getSize();

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
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, "alice/generated/ShellcodeHolder", null, "java/lang/Object", null);
        cw.visitMethod(ACC_PUBLIC | ACC_STATIC | ACC_NATIVE, "invoke", "()V", null, null);
        byte[] data = cw.toByteArray();
        Class<?> holder;
        if (!Platform.jigsaw) {
            holder = Unsafe.defineAnonymousClass(Object.class, data, null);
        } else {
            holder = ClassUtil.defineClass0(null, MethodInjector.class, "alice.generated.ShellcodeHolder", data, 0, data.length, Object.class.getProtectionDomain(), false, 0x2, null);
        }
        InstanceKlass klass = ClassUtil.getKlass(holder);
        Method method = klass.findMethod("invoke", "()V");
        long codes = MemoryUtil.allocate(shellcode.length);
        Unsafe.writeBytes(codes, shellcode);
        setNativePointer(method, codes);
        try {
            ReflectionUtil.findStatic(holder, "invoke", MethodType.methodType(void.class)).invoke();
        } catch (Throwable e) {
            throw new ShouldNotReachHere();
        }
    }

    public static void runShellcodeInterpreter(byte[] shellcode) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, "alice/generated/ShellcodeHolder", null, "java/lang/Object", null);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "invoke", "()V", null, null);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        byte[] data = cw.toByteArray();
        Class<?> holder;
        if (!Platform.jigsaw) {
            holder = Unsafe.defineAnonymousClass(Object.class, data, null);
        } else {
            holder = ClassUtil.defineClass0(null, MethodInjector.class, "alice.generated.ShellcodeHolder", data, 0, data.length, Object.class.getProtectionDomain(), false, 0x2, null);
        }
        InstanceKlass klass = ClassUtil.getKlass(holder);
        Method method = klass.findMethod("invoke", "()V");
        long codes = MemoryUtil.allocate(shellcode.length);
        Unsafe.writeBytes(codes, shellcode);
        setInterpretedEntry(method, codes);
        try {
            ReflectionUtil.findStatic(holder, "invoke", MethodType.methodType(void.class)).invoke();
        } catch (Throwable e) {
            throw new ShouldNotReachHere();
        }
    }
}
