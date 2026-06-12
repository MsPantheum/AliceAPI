package alice.util;

import alice.HSDB;
import alice.Platform;
import alice.exception.ShouldNotReachHere;
import org.objectweb.asm.Type;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal;
import sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal;
import sun.jvm.hotspot.debugger.windbg.WindbgDebuggerLocal;
import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.runtime.VMObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public final class Converter {

    public static sun.jvm.hotspot.oops.Method convert(MethodInfo info) {
        InstanceKlass klass = ClassUtil.getKlass(info.holder);
        return klass.findMethod(info.name, info.descriptor);
    }

    public static MethodHandle convert(sun.jvm.hotspot.oops.Method method) {
        try {
            Class<?> holder = MemoryUtil.getObject(method.getMethodHolder().getJavaMirror().getHandle());
            String name = method.getName().asString();
            Type type = Type.getMethodType(method.getSignature().asString());
            Class<?> ret = convert(type.getReturnType().getClassName());

            Type[] arg_types = type.getArgumentTypes();
            Class<?>[] args = new Class<?>[arg_types.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = convert(arg_types[i].getClassName());
            }
            if (method.getAccessFlagsObj().isStatic()) {
                return ReflectionUtil.findStatic(holder, name, MethodType.methodType(ret, args));
            } else {
                return ReflectionUtil.findVirtual(holder, name, MethodType.methodType(ret, args));
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static Class<?> convert(String name) throws ClassNotFoundException {
        switch (name) {
            case "byte": {
                return byte.class;
            }
            case "short": {
                return short.class;
            }
            case "int": {
                return int.class;
            }
            case "long": {
                return long.class;
            }
            case "float": {
                return float.class;
            }
            case "double": {
                return double.class;
            }
            case "char": {
                return char.class;
            }
            case "boolean": {
                return boolean.class;
            }
            case "void": {
                return void.class;
            }
            default: {
                return Class.forName(name);
            }
        }
    }

    public static MethodHandle unreflect(Method method) {
        try {
            return ReflectionUtil.lookup().unreflect(method);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getAddressValue(Address addr) {
        return HSDB.debugger.getAddressValue(addr);
    }

    public static long getAddressValue(VMObject vmObject) {
        return getAddressValue(vmObject.getAddress());
    }

    public static Address toAddress(long addr) {
        if (Platform.linux) {
            return ((LinuxDebuggerLocal) HSDB.debugger).newAddress(addr);
        } else if (Platform.win32) {
            return ((WindbgDebuggerLocal) HSDB.debugger).newAddress(addr);
        } else if (Platform.bsd || Platform.darwin) {
            return ((BsdDebuggerLocal) HSDB.debugger).newAddress(addr);
        }
        throw new ShouldNotReachHere();
    }
}
