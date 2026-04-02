package alice.injector.patch;

import alice.LaunchWrapper;
import alice.util.ClassUtil;
import alice.util.DebugUtil;
import alice.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UniversalPatcher implements Opcodes {

    private static final Map<String, byte[]> _protected = new HashMap<>();

    static {
        if (!DebugUtil.isRunningTest()) {
            try (JarFile jar = new JarFile(ClassUtil.getJarPath(LaunchWrapper.class))) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        _protected.put(entry.getName(), IOUtils.toByteArray(jar.getInputStream(entry)));
                    }
                }
            } catch (IOException e) {
                DebugUtil.printThrowableFully(e);
            }
        }
    }

    public static byte[] patch(byte[] data, String name) {
        if (data == null) {
            return null;
        }

        if (_protected.containsKey(name)) {
            return _protected.get(name);
        }

        final boolean[] changed = {false};
        ClassReader cr = new ClassReader(data);

        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int _access, String _name, String _descriptor, String _signature, String[] _exceptions) {
                return new MethodVisitor(ASM5, cv.visitMethod(_access, _name, _descriptor, _signature, _exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (owner.equals("sun/misc/Unsafe")) {
                            if (name.equals("putInt") || name.equals("putIntVolatile")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/UnsafeInterceptor";
                                descriptor = "(Lsun/misc/Unsafe;" + descriptor.substring(1);
                            }
                        } else if (opcode == INVOKESPECIAL && _name.equals("<init>") && name.equals("<init>")) {
                            if (owner.equals("java/net/URLClassLoader") || owner.equals("sun/applet/AppletClassLoader")) {
                                changed[0] = true;
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                super.visitVarInsn(ALOAD, 0);
                                super.visitMethodInsn(INVOKESTATIC, "alice/interceptor/URLClassLoaderInterceptor", "creation", "(Ljava/net/URLClassLoader;)Ljava/net/URLClassLoader;", false);
                                super.visitInsn(POP);
                                return;
                            }
                        } else if (owner.equals("java/lang/reflect/Method")) {
                            if (name.equals("invoke")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/ReflectionInterceptor";
                                descriptor = "(Ljava/lang/reflect/Method;" + descriptor.substring(1);
                            }
                        } else if (owner.equals("java/lang/reflect/Field")) {
                            if (name.equals("set")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/ReflectionInterceptor";
                                descriptor = "(Ljava/lang/reflect/Field;" + descriptor.substring(1);
                            }
                        } else if (owner.equals("java/lang/invoke/MethodHandle")) {
                            if (name.equals("invoke") || name.equals("invokeExact")) {
                                changed[0] = true;
                                opcode = INVOKESTATIC;
                                owner = "alice/interceptor/MethodHandleInterceptor";
                                descriptor = "(Ljava/lang/invoke/MethodHandle;" + descriptor.substring(1);
                            }
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        super.visitFieldInsn(opcode, owner, name, descriptor);
                    }

                };
            }
        };

        cr.accept(cv, 0);
        byte[] ret = cw.toByteArray();
        if (changed[0]) {
            FileUtil.write(name.replace('/', '.'), ret);
        }
        return changed[0] ? ret : data;
    }
}
