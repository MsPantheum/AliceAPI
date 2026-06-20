package alice.util;

import alice.Platform;
import alice.exception.ShouldNotReachHere;
import alice.log.Logger;
import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class BytecodeUtil implements Opcodes {

    /**
     * The Object type.
     */
    public static final Type OBJECT_TYPE = Type.getType("Ljava/lang/Object;");

    public static String adapt(String s) {
        return s.replace('(', '_').replace(')', '_').replace('/', '_').replace(';', '_');
    }

    /**
     * Patch a class with the given ClassVisitor.
     *
     * @param original   the original class bytes
     * @param cvFunction used to provide your ClassVisitor, you should pass the ClassWriter into its constructor.
     * @return patched class bytes
     */
    public static byte[] patchClass(byte[] original, Function<ClassWriter, ClassVisitor> cvFunction) {
        ClassReader cr = new ClassReader(original);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = cvFunction.apply(cw);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    /**
     * Generate a toString invocation. Note that we won't do null check.
     *
     * @param mv the method to inject
     */
    public static void toString(MethodVisitor mv) {
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
    }

    /**
     * Generate a println invocation which prints the string on top of the stack.<br>You should ensure that there's a Ljava/lang/String; on top of the stack yourself.
     *
     * @param mv the method to inject
     */
    public static void println(MethodVisitor mv) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitInsn(SWAP);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    /**
     * Generate an invocation to log the string on top of the stack.<br>You should ensure that there's a Ljava/lang/String; on top of the stack yourself.
     *
     * @param mv    the method to inject
     * @param level the log level
     */
    public static void log(MethodVisitor mv, Logger.LogLevel level) {
        mv.visitFieldInsn(GETSTATIC, "alice/log/Logger", "MAIN", "Lalice/log/Logger;");
        mv.visitInsn(SWAP);
        mv.visitMethodInsn(INVOKEVIRTUAL, "alice/log/Logger", level.toString(), "(Ljava/lang/String;)V", false);
    }

    /**
     * Generate a println invocation which prints a given string.
     *
     * @param mv      the method to inject
     * @param message the str to print
     */
    public static void println(MethodVisitor mv, String message) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    /**
     * Generate a println invocation which prints an object read from local variables.
     *
     * @param mv    the method to inject
     * @param index the index of the local variable
     */
    public static void println(MethodVisitor mv, int index) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ALOAD, index);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    }

    /**
     * Generate an invocation to log a message.
     *
     * @param mv      the method to inject
     * @param level   the log level
     * @param message the log message
     */
    public static void log(MethodVisitor mv, Logger.LogLevel level, String message) {
        mv.visitFieldInsn(GETSTATIC, "alice/log/Logger", "MAIN", "Lalice/log/Logger;");
        mv.visitLdcInsn(message);
        mv.visitMethodInsn(INVOKEVIRTUAL, "alice/log/Logger", level.toString(), "(Ljava/lang/String;)V", false);
    }

    /**
     * Automatically push an integer into the stack using the best operation.
     *
     * @param mv      the method to inject
     * @param integer the integer to push into stack
     */
    public static void pushInt(MethodVisitor mv, int integer) {
        if (integer < 6) {
            mv.visitInsn(ICONST_0 + integer);
        } else if (integer < 128) {
            mv.visitIntInsn(BIPUSH, integer);
        } else if (integer < 32768) {
            mv.visitIntInsn(SIPUSH, integer);
        } else {
            mv.visitLdcInsn(integer);
        }
    }

    /**
     * Create a new array with the custom dimension and the given type.
     *
     * @param mv        the method to inject
     * @param dimension the dimension of the array
     * @param size      the size of the array
     * @param type      the array type, see {@link Type}
     */
    public static void createArray(MethodVisitor mv, int dimension, int[] size, Type type) {
        assert dimension == size.length;
        for (int i : size) {
            pushInt(mv, i);
        }
        StringBuilder name = new StringBuilder(type.getInternalName());
        for (int i = 0; i < dimension; i++) {
            name.insert(0, '[');
        }
        mv.visitMultiANewArrayInsn(name.toString(), dimension);
    }

    /**
     * Create a new 1-dimension array with the given type.
     *
     * @param mv   the method to inject
     * @param size the size of the array
     * @param type the array type, see {@link Type}
     */
    public static void createArray(MethodVisitor mv, int size, Type type) {
        if (size < 0) {
            throw new NegativeArraySizeException();
        }

        //Load the array size
        pushInt(mv, size);

        int sort = type.getSort();

        switch (sort) {
            case Type.OBJECT: {
                mv.visitTypeInsn(ANEWARRAY, type.getInternalName());
                break;
            }
            case Type.BOOLEAN: {
                mv.visitIntInsn(NEWARRAY, T_BOOLEAN);
                break;
            }
            case Type.CHAR: {
                mv.visitIntInsn(NEWARRAY, T_CHAR);
                break;
            }
            case Type.FLOAT: {
                mv.visitIntInsn(NEWARRAY, T_FLOAT);
                break;
            }
            case Type.DOUBLE: {
                mv.visitIntInsn(NEWARRAY, T_DOUBLE);
                break;
            }
            case Type.BYTE: {
                mv.visitIntInsn(NEWARRAY, T_BYTE);
                break;
            }
            case Type.SHORT: {
                mv.visitIntInsn(NEWARRAY, T_SHORT);
                break;
            }
            case Type.INT: {
                mv.visitIntInsn(NEWARRAY, T_INT);
                break;
            }
            case Type.LONG: {
                mv.visitIntInsn(NEWARRAY, T_LONG);
                break;
            }
            default: {
                throw new IllegalArgumentException(String.valueOf(sort));
            }
        }
    }

    public static void boxing(MethodVisitor mv, Type type) {
        String class_name = primitive2Boxed(type);
        class_name = "java/lang/" + Character.toUpperCase(class_name.charAt(0)) + class_name.substring(1);
        mv.visitMethodInsn(INVOKESTATIC, class_name, "valueOf", '(' + type.getInternalName() + ")L" + class_name + ';', false);
    }

    public static void unboxing(MethodVisitor mv, Type type) {
        switch (type.getInternalName()) {
            case "java/lang/Boolean": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                break;
            }
            case "java/lang/Byte": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                break;
            }
            case "java/lang/Character": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                break;
            }
            case "java/lang/Short": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                break;
            }
            case "java/lang/Integer": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                break;
            }
            case "java/lang/Long": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                break;
            }
            case "java/lang/Float": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                break;
            }
            case "java/lang/Double": {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private static String primitive2Boxed(Type type) {
        String name = type.getInternalName();
        switch (name) {
            case "byte":
                return "java/lang/Byte";
            case "short":
                return "java/lang/Short";
            case "int":
                return "java/lang/Integer";
            case "long":
                return "java/lang/Long";
            case "float":
                return "java/lang/Float";
            case "double":
                return "java/lang/Double";
            case "char":
                return "java/lang/Character";
            case "boolean":
                return "java/lang/Boolean";
            default:
                throw new IllegalArgumentException(name.concat(" isn't a primitive type!"));
        }
    }

    private static final Type[] SORT_TO_TYPE = {Type.VOID_TYPE,    // 0
            Type.BOOLEAN_TYPE, // 1
            Type.CHAR_TYPE,    // 2
            Type.BYTE_TYPE,    // 3
            Type.SHORT_TYPE,   // 4
            Type.INT_TYPE,     // 5
            Type.FLOAT_TYPE,   // 6
            Type.LONG_TYPE,    // 7
            Type.DOUBLE_TYPE   // 8
    };

    /**
     * Pop all the contents on the stack according to given types. Only for clear the context of an INVOKE* opcode.
     * Note that the order of args will be reversed due to the JVM invocation order.
     *
     * @param mv   the method to inject
     * @param args types of things we need to pop
     */
    public static void popVariables(MethodVisitor mv, Type... args) {
        for (int i = args.length - 1; i > -1; i--) {
            Type arg = args[i];
            mv.visitInsn(arg.getSize() == 1 ? POP : POP2);
        }
    }

    public static void registerValueProvider(String type, Consumer<MethodVisitor> consumer) {
        valueProviders.put(type, consumer);
    }

    private static final Map<String, Consumer<MethodVisitor>> valueProviders = new HashMap<>();

    static {
        valueProviders.put("java/lang/String", mv -> mv.visitLdcInsn(""));
        valueProviders.put("java/lang/Integer", mv -> {
            mv.visitInsn(ICONST_0);
            boxing(mv, Type.INT_TYPE);
        });
        valueProviders.put("java/lang/Boolean", mv -> {
            mv.visitInsn(ICONST_0);
            boxing(mv, Type.BOOLEAN_TYPE);
        });
        valueProviders.put("java/lang/Byte", mv -> {
            mv.visitInsn(ICONST_0);
            boxing(mv, Type.BYTE_TYPE);
        });
        valueProviders.put("java/lang/Short", mv -> {
            mv.visitInsn(ICONST_0);
            boxing(mv, Type.SHORT_TYPE);
        });
        valueProviders.put("java/lang/Long", mv -> {
            mv.visitInsn(LCONST_0);
            boxing(mv, Type.LONG_TYPE);
        });
        valueProviders.put("java/lang/Float", mv -> {
            mv.visitInsn(FCONST_0);
            boxing(mv, Type.FLOAT_TYPE);
        });
        valueProviders.put("java/lang/Double", mv -> {
            mv.visitInsn(DCONST_0);
            boxing(mv, Type.DOUBLE_TYPE);
        });
        valueProviders.put("java/lang/Character", mv -> {
            mv.visitInsn(ICONST_0);
            boxing(mv, Type.CHAR_TYPE);
        });
        valueProviders.put("java/util/Iterator", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyIterator", "Ljava/util/Iterator;", false));
        valueProviders.put("java/util/ListIterator", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyListIterator", "Ljava/util/ListIterator;", false));
        valueProviders.put("java/util/NavigableMap", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyNavigableMap", "Ljava/util/NavigableMap;", false));
        valueProviders.put("java/util/SortedMap", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptySortedMap", "Ljava/util/SortedMap;", false));
        valueProviders.put("java/util/Map", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyMap", "Ljava/util/Map;", false));
        valueProviders.put("java/util/List", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyList", "Ljava/util/List;", false));
        valueProviders.put("java/util/NavigableSet", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyNavigableSet", "Ljava/util/NavigableSet;", false));
        valueProviders.put("java/util/SortedSet", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptySortedSet", "Ljava/util/SortedSet;", false));
        valueProviders.put("java/util/Set", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptySet", "Ljava/util/Set;", false));
        valueProviders.put("java/util/Enumeration", mv -> mv.visitMethodInsn(INVOKESTATIC, "java/lang/Collections", "emptyEnumeration", "Ljava/util/Enumeration;", false));

    }

    public static void generateValue(MethodVisitor mv, Type type) {
        int sort = type.getSort();
        switch (sort) {
            case Type.VOID: {
                return;
            }
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT: {
                mv.visitInsn(ICONST_0);
                break;
            }
            case Type.FLOAT: {
                mv.visitInsn(FCONST_0);
                break;
            }
            case Type.LONG: {
                mv.visitInsn(LCONST_0);
                break;
            }
            case Type.DOUBLE: {
                mv.visitInsn(DCONST_0);
                break;
            }
            case Type.ARRAY: {
                if (type.getDimensions() == 1) {
                    createArray(mv, 0, type);
                } else {
                    createArray(mv, type.getDimensions(), new int[type.getDimensions()], type);
                }
                break;
            }
            case Type.OBJECT: {
                Consumer<MethodVisitor> _try = valueProviders.get(type.getInternalName());
                if (_try != null) {
                    _try.accept(mv);
                } else {
                    mv.visitInsn(ACONST_NULL);
                }
                break;
            }
            default: {
                throw new ShouldNotReachHere();
            }
        }
    }

    /**
     * Patch methods in a class
     *
     * @param classBytes      the class to patch
     * @param methodProcessor do your patches
     * @return the patched class
     */
    public static byte[] patchMethods(byte[] classBytes, Function<MethodVisitor, MethodVisitor> methodProcessor) {
        return patchMethod(classBytes, null, methodProcessor);
    }

    /**
     * Patch a specific method in a class.
     *
     * @param classBytes      the class to patch
     * @param target          the target method, name and descriptor, pass null to apply to all methods
     * @param methodProcessor do your patches
     * @return the patched class
     */
    public static byte[] patchMethod(byte[] classBytes, String target, Function<MethodVisitor, MethodVisitor> methodProcessor) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ClassVisitor(Platform.ASM_LEVEL, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return target == null ? methodProcessor.apply(mv) : target.equals(name.concat(descriptor)) ? methodProcessor.apply(mv) : mv;
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public static byte[] searchString(byte[] clazz, String target) {
        return patchClass(clazz, cw -> new StringSearcher(cw, target));
    }

    private static final class StringSearcher extends ClassVisitor {

        private final String target;
        private String className;

        private StringSearcher(ClassVisitor classVisitor, String target) {
            super(Platform.ASM_LEVEL, classVisitor);
            this.target = target;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            className = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return new MethodVisitor(Platform.ASM_LEVEL, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String) {
                        if (((String) value).contains(target)) {
                            Logger.MAIN.debug("[StringSearcher] Find target ".concat(target).concat(" in ".concat(className).concat(".").concat(name).concat(descriptor).concat(".")));
                        }
                    }
                    super.visitLdcInsn(value);
                }
            };
        }
    }
}
