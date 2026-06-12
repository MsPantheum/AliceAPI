package alice.util;

import alice.Platform;
import sun.jvm.hotspot.types.Field;
import sun.jvm.hotspot.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public final class DebugUtil {

    public static boolean DEBUG = false;

    private static volatile PrintStream out;
    private static volatile PrintStream err;

    private static final PrintStream NULL;

    static {
        try {
            //noinspection IOStreamConstructor nio can't handle NUL file.
            NULL = new PrintStream(Platform.win32 ? new FileOutputStream("NUL") : Files.newOutputStream(Paths.get("/dev/null")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disableSystemOutput() {
        out = System.out;
        err = System.err;
        System.setOut(NULL);
        System.setErr(NULL);
    }

    public static void restoreSystemOutput() {
        if (out == null || err == null) {
            throw new IllegalStateException();
        }
        System.setOut(out);
        System.setErr(err);
    }

    public static void setOutFile(String path) {
        try {
            System.setOut(new PrintStream(Files.newOutputStream(Paths.get(path))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setErrFile(String path) {
        try {
            System.setErr(new PrintStream(Files.newOutputStream(Paths.get(path))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isRunningTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return DEBUG;
    }

    public static void printThrowableFully(Throwable t) {
        printThrowableFully(t, System.err);
    }

    public static void printThrowableFully(Throwable t, PrintStream out) {
        int depth = 0;
        while (t != null) {
            out.println("Exception depth " + depth + ":" + t.getClass().getName() + " Message:" + t.getMessage());
            for (StackTraceElement ste : t.getStackTrace()) {
                out.println(ste.toString());
            }
            t = t.getCause();
            depth++;
        }
    }

    public static StackTraceElement getCaller() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static void printType(Type type) {
        printType(type, System.out);
    }

    public static void printType(Type type, PrintStream out) {
        out.println(type.getName().concat(type.getSuperclass() != null ? " extends ".concat(type.getSuperclass().getName()) : ""));
        out.println("size=".concat(String.valueOf(type.getSize())));
        @SuppressWarnings("unchecked") Iterator<Field> fields = type.getFields();
        if (fields != null) {
            fields.forEachRemaining(field -> out.println("\t".concat(field.isStatic() ? "static " : "").concat(field.getName()).concat(" ").concat(field.getType().getName()).concat(" size=").concat(String.valueOf(field.getSize())).concat(field.isStatic() ? "" : " offset=".concat(String.valueOf(field.getOffset())))));
        }
    }

    public static String toString(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k.toString()).append("\t").append(v.toString()).append("\n");
        });
        return sb.toString();
    }

    public static String toString(Collection<?> collection) {
        StringBuilder sb = new StringBuilder();
        collection.forEach(v -> {
            sb.append(v.toString()).append("\n");
        });
        return sb.toString();
    }
}
