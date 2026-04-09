package java.lang.invoke;


import java.lang.reflect.Method;

public class MethodHandles {
    public static final class Lookup {

        public MethodHandleInfo revealDirect(MethodHandle target) {
            return null;
        }

        public MethodHandle findVirtual(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
            return null;
        }

        public MethodHandle findStatic(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
            return null;
        }

        public MethodHandle findConstructor(Class<?> refc, MethodType type) throws NoSuchMethodException, IllegalAccessException {
            return null;
        }

        public MethodHandle findSpecial(Class<?> refc, String name, MethodType type,
                                        Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
            return null;
        }

        public VarHandle findVarHandle(Class<?> recv, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
            return null;
        }

        public VarHandle findStaticVarHandle(Class<?> decl, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
            return null;
        }

        public MethodHandle unreflect(Method m) throws IllegalAccessException {
            return null;
        }
    }
}
