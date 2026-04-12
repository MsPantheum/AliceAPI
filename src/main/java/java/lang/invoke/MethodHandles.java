package java.lang.invoke;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static java.lang.invoke.MethodHandleNatives.Constants.NESTMATE_CLASS;
import static java.lang.invoke.MethodHandleNatives.Constants.STRONG_LOADER_LINK;

public class MethodHandles {
    public static final class Lookup {

        public static final int PUBLIC = Modifier.PUBLIC;
        public static final int PRIVATE = Modifier.PRIVATE;
        public static final int PROTECTED = Modifier.PROTECTED;
        public static final int PACKAGE = Modifier.STATIC;
        public static final int MODULE = PACKAGE << 1;
        public static final int UNCONDITIONAL = PACKAGE << 2;
        public static final int ORIGINAL = PACKAGE << 3;
        private static final int ALL_MODES = (PUBLIC | PRIVATE | PROTECTED | PACKAGE | MODULE | UNCONDITIONAL | ORIGINAL);
        private static final int FULL_POWER_MODES = (ALL_MODES & ~UNCONDITIONAL);
        private static final int TRUSTED = -1;

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

        public int lookupModes() {
            return 0;
        }

        public Lookup defineHiddenClassWithClassData(byte[] bytes, Object classData, boolean initialize, ClassOption... options) {
            return null;
        }

        public Lookup defineHiddenClass(byte[] bytes, boolean initialize, ClassOption... options) {
            return null;
        }

        public enum ClassOption {
            NESTMATE(NESTMATE_CLASS),
            STRONG(STRONG_LOADER_LINK);
            private final int flag;

            ClassOption(int flag) {
                this.flag = flag;
            }

            static int optionsToFlag(Set<ClassOption> options) {
                int flags = 0;
                for (ClassOption cp : options) {
                    flags |= cp.flag;
                }
                return flags;
            }
        }
    }
}
