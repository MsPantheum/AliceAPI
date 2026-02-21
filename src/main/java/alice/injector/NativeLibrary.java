package alice.injector;

import alice.util.*;
import net.fornwall.jelf.ElfFile;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import static alice.util.ProcReader.parseProcMaps;

public class NativeLibrary {

    private static final LinkedHashMap<String, NativeLibrary> libraries = new LinkedHashMap<>();

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
            nativeLibraryContext = Unsafe.getObject(Unsafe.staticFieldBase(f), Unsafe.staticFieldOffset(f));
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final Object LIBRARY_INSTANCE;
    private final String path;
    private final boolean isBuiltin;
    private final long handle;
    private final long base;

    public static NativeLibrary load(String name, boolean isBuiltin) {
        name = Paths.get(name).toAbsolutePath().toString();
        if (libraries.containsKey(name)) {
            return libraries.get(name);
        } else if (canLoad(name)) {
            NativeLibrary ret = new NativeLibrary(name, isBuiltin);
            libraries.put(name, ret);
            return ret;
        }
        return null;
    }

    private NativeLibrary(String path, boolean isBuiltin) {
        this.path = path;
        this.isBuiltin = isBuiltin;
        LIBRARY_INSTANCE = Unsafe.allocateInstance(NATIVE_LIBRARY_CLASS);
        Unsafe.putObject(LIBRARY_INSTANCE, name_offset, path);
        Unsafe.putInt(LIBRARY_INSTANCE, isBuiltin_offset, 0);
        Unsafe.putObject(LIBRARY_INSTANCE, fromClass_offset, Object.class);
        nativeLibraryContext.add(LIBRARY_INSTANCE);
        load();
        handle = Unsafe.getLong(LIBRARY_INSTANCE, handle_offset);
        List<ProcReader.MemoryMapping> maps = parseProcMaps(ProcessUtil.getPID());
        long _base = 0;
        for (ProcReader.MemoryMapping map : maps) {
            if (map.pathname.equals(path)) {
                _base = Long.parseLong(map.addressRangeStart, 16);
                break;
            }
        }
        if (_base == 0) {
            throw new RuntimeException("Cannot get base address of " + path + "!");
        }
        base = _base;
    }

    private static final MethodHandle load = ReflectionUtil.findVirtual(NATIVE_LIBRARY_CLASS, "load", MethodType.methodType(void.class, String.class, boolean.class));
    private static final MethodHandle find = ReflectionUtil.findVirtual(NATIVE_LIBRARY_CLASS, "find", MethodType.methodType(long.class, String.class));

    private void load() {
        try {
            load.invoke(LIBRARY_INSTANCE, path, isBuiltin);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public long find(String symbol) {
        try {
            return (long) find.invoke(LIBRARY_INSTANCE, symbol);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public long getHandle() {
        return handle;
    }

    public long getBase() {
        return base;
    }

    public static boolean canLoad(String filePath) {
        if (filePath.isEmpty() || libraries.containsKey(filePath) || !FileUtil.exists(filePath)) {
            return false;
        }

        final byte[] data = FileUtil.read(filePath);

        if (data[0] != 0x7f || data[1] != 0x45 || data[2] != 0x4c || data[3] != 0x46) {
            System.out.println(filePath + " isn't elf object.");
            return false;
        }

        ElfFile elf = ElfFile.from(data);
        if (elf.e_type != 0x03) {
            return false;
        }

        for (int i = 0; i < elf.e_phnum; i++) {
            if (elf.getProgramHeader(i).p_type == 0x03) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return path + " base=" + base;
    }
}
