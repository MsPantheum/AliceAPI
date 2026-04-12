package alice.util;

import alice.Platform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DebugUtil {

    public static boolean DEBUG = false;

    public static final boolean LOG_KLASS_REPLACE = "true".equals(System.getProperty("alice.debug.log.replace_klass"));
    public static final boolean LOG_UCP_REPLACE = "true".equals(System.getProperty("alice.debug.log.replace_ucp"));
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

    public static void disableOutput() {
        out = System.out;
        err = System.err;
        System.setOut(NULL);
        System.setErr(NULL);
    }

    public static void restoreOutput() {
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
            for (int i = 0; i < depth; i++) {
                out.print('\t');
            }
            out.println("Exception depth " + depth + ":" + t.getClass().getName() + " Message:" + t.getMessage());
            for (StackTraceElement ste : t.getStackTrace()) {
                for (int i = 0; i < depth; i++) {
                    out.print('\t');
                }
                out.println(ste);
            }
            t = t.getCause();
            depth++;
        }
    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static void println(Object o) {
        System.out.println(o.getClass().getName());
    }

    public static void printStackTrace() {
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            System.out.println(ste.toString());
        }
    }
}
