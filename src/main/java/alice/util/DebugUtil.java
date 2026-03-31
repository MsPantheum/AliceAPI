package alice.util;

import alice.Platform;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DebugUtil {

    private static volatile PrintStream out;
    private static volatile PrintStream err;

    private static final PrintStream NULL;

    static {
        try {
            NULL = new PrintStream(Files.newOutputStream(Paths.get(Platform.win32 ? "NUL" : "/dev/null")));
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
        return false;
    }

    public static void printThrowableFully(Throwable t) {
        printThrowableFully(t, System.err);
    }

    public static void printThrowableFully(Throwable t, PrintStream out) {
        int depth = 0;
        while (t != null) {
            out.println("Exception depth " + depth + ":" + t.getClass().getName() + " Message:" + t.getMessage());
            for (StackTraceElement ste : t.getStackTrace()) {
                out.println(ste);
            }
            t = t.getCause();
        }
    }
}
