package alice.util;

import alice.Platform;
import alice.log.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DebugUtil {

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
            Logger.MAIN.debug("Exception depth " + depth + ":" + t.getClass().getName() + " Message:" + t.getMessage());
            for (StackTraceElement ste : t.getStackTrace()) {
                Logger.MAIN.debug(ste.toString());
            }
            t = t.getCause();
            depth++;
        }
    }

    public static void printStackTrace() {
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            Logger.MAIN.debug(ste.toString());
        }
    }
}
