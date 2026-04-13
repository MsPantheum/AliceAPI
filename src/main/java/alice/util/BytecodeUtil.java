package alice.util;

import org.objectweb.asm.*;

import java.util.function.Function;

public class BytecodeUtil implements Opcodes {

    public static final Type OBJECT_TYPE = Type.getType("Ljava/lang/Object;");

    /**
     * Patch a class with the given ClassVisitor.
     *
     * @param original   the original class bytes
     * @param cvFunction provide your ClassVisitor, you should pass the ClassWriter into its constructor.
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
     * Generate a println invoke which prints a given string.
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
     * Generate a println invoke which prints a object read from local variables.
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
     * Automatically push an integer into stack using the best operation.
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
     * Create a new array with custom dimension with given type.
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
     * Create a new 1 dimension array with given type.
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
        String internalName = type.getInternalName();
        if (internalName.length() != 1 || internalName.equals("V")) {
            throw new IllegalArgumentException();
        }
        String class_name = type.getClassName();
        class_name = "java/lang/" + Character.toUpperCase(class_name.charAt(0)) + class_name.substring(1);
        mv.visitMethodInsn(INVOKESTATIC, class_name, "valueOf", '(' + type.getInternalName() + ")L" + class_name + ';', false);
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
     * Pop all the contents on stack according to given types. Only for clear the context of a INVOKE* opcode.
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

    public static void generateValue(MethodVisitor mv, Type type) {
        int sort = type.getSort();
        switch (sort) {
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
                //TODO generate values for some common type like String,List,etc...
                mv.visitInsn(ACONST_NULL);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
