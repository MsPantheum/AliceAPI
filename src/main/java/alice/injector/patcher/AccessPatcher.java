package alice.injector.patcher;

import alice.Platform;
import alice.util.BytecodeUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Function;

public class AccessPatcher {

    public static void addClassTarget(String className, int neo_access) {
        classTargets.put(className, neo_access);
    }

    public static void addFieldTarget(String className, String fieldName, String descriptor, int neo_access) {
        Object2IntMap<String> fts = fieldTargets.computeIfAbsent(className, createOrGet);
        fts.put(fieldName.concat(descriptor), neo_access);
    }

    public static void addMethodTarget(String className, String methodName, String descriptor, int neo_access) {
        Object2IntMap<String> mts = methodTargets.computeIfAbsent(className, createOrGet);
        mts.put(methodName.concat(descriptor), neo_access);
    }

    private static final Function<String, Object2IntMap<String>> createOrGet = (k) -> {
        Object2IntMap<String> map = new Object2IntOpenHashMap<>();
        map.defaultReturnValue(0xffff);
        return map;
    };

    private static final Object2IntMap<String> classTargets = new Object2IntOpenHashMap<>();
    private static final Object2ObjectMap<String, Object2IntMap<String>> fieldTargets = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<String, Object2IntMap<String>> methodTargets = new Object2ObjectOpenHashMap<>();

    static {
        classTargets.defaultReturnValue(0xffff);
    }

    public static byte[] transform(byte[] data, String class_name) {
        int class_access = classTargets.removeInt(class_name);
        Object2IntMap<String> fts = fieldTargets.remove(class_name);
        Object2IntMap<String> mts = methodTargets.remove(class_name);
        boolean change_class_access = class_access != 0xffff;
        boolean change_field_access = fts != null;
        boolean change_method_access = mts != null;
        if (change_class_access || change_field_access || change_method_access) {
            return BytecodeUtil.patchClass(data, cw -> new ClassVisitor(Platform.ASM_LEVEL, cw) {
                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    if (change_class_access) {
                        access = class_access;
                    }
                    super.visit(version, access, name, signature, superName, interfaces);
                }

                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                    if (change_field_access) {
                        int _try = fts.removeInt(name.concat(descriptor));
                        access = _try != 0xffff ? _try : access;
                    }
                    return super.visitField(access, name, descriptor, signature, value);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    if (change_method_access) {
                        int _try = mts.removeInt(name.concat(descriptor));
                        access = _try != 0xffff ? _try : access;
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            });
        }
        return data;
    }
}
