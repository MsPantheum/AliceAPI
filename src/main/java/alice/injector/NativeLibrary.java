package alice.injector;

import alice.util.ProcReader;
import alice.util.ProcessUtil;
import alice.util.ReflectionUtil;
import alice.util.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Stack;

import static alice.util.ProcReader.parseProcMaps;

public class NativeLibrary {

    private static final Class<?> NATIVE_LIBRARY_CLASS;
    private static final long name_offset;
    private static final long isBuiltin_offset;
    private static final long fromClass_offset;
    private static final long handle_offset;

    private static final Stack<Object> nativeLibraryContext;

    static {
        try {
            NATIVE_LIBRARY_CLASS = ClassLoader.getSystemClassLoader().loadClass("java.lang.ClassLoader$NativeLibrary");
            Field f = NATIVE_LIBRARY_CLASS.getDeclaredField("name");
            name_offset = Unsafe.objectFieldOffset(f);
            f = NATIVE_LIBRARY_CLASS.getDeclaredField("isBuiltin");
            isBuiltin_offset = Unsafe.objectFieldOffset(f);
            f = NATIVE_LIBRARY_CLASS.getDeclaredField("fromClass");
            fromClass_offset = Unsafe.objectFieldOffset(f);
            f = NATIVE_LIBRARY_CLASS.getDeclaredField("handle");
            handle_offset = Unsafe.objectFieldOffset(f);
            f = ClassLoader.class.getDeclaredField("nativeLibraryContext");
            nativeLibraryContext = (Stack<Object>) Unsafe.getObject(Unsafe.staticFieldBase(f),Unsafe.staticFieldOffset(f));
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final Object LIBRARY_INSTANCE;
    private final String path;
    private final boolean isBuiltin;
    private final long handle;
    private final long base;

    public NativeLibrary(String path,boolean isBuiltin){
        this.path = path;
        this.isBuiltin = isBuiltin;
        LIBRARY_INSTANCE = Unsafe.allocateInstance(NATIVE_LIBRARY_CLASS);
        Unsafe.putObject(LIBRARY_INSTANCE,name_offset,path);
        Unsafe.putInt(LIBRARY_INSTANCE,isBuiltin_offset,0);
        Unsafe.putObject(LIBRARY_INSTANCE,fromClass_offset,Object.class);
        nativeLibraryContext.add(LIBRARY_INSTANCE);
        load();
        handle = Unsafe.getLong(LIBRARY_INSTANCE,handle_offset);
        List<ProcReader.MemoryMapping> maps = parseProcMaps(ProcessUtil.getPID());
        long _base = 0;
        for (ProcReader.MemoryMapping map : maps) {
            if(map.pathname.equals(path)){
                _base = Long.parseLong(map.addressRangeStart,16);
                break;
            }
        }
        if(_base == 0){
            throw new RuntimeException("Cannot get base address of " + path + "!");
        }
        base = _base;
    }

    private static final MethodHandle load = ReflectionUtil.findVirtual(NATIVE_LIBRARY_CLASS,"load", MethodType.methodType(void.class,String.class,boolean.class));
    private static final MethodHandle find = ReflectionUtil.findVirtual(NATIVE_LIBRARY_CLASS,"find", MethodType.methodType(long.class,String.class));

    private void load(){
        try {
            load.invoke(LIBRARY_INSTANCE,path,isBuiltin);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public long find(String symbol){
        try {
            return (long) find.invoke(LIBRARY_INSTANCE,symbol);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public long getHandle() {
        return handle;
    }
}
