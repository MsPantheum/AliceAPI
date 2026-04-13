package alice.log;

import alice.util.FileUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple logger.
 */
public class Logger extends Thread {

    public static final Logger MAIN = new Logger("Alice");

    private static boolean running = true;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path path;
    private final List<String> lines = new ArrayList<>();

    public Logger(String name) {
        path = Paths.get("alice_logs").resolve(name + ".log");
        if (!FileUtil.exists(path)) {
            FileUtil.createFile(path);
        } else if (FileUtil.isDirectory(path)) {
            throw new IllegalStateException();
        } else {
            FileUtil.write(path, "".getBytes(StandardCharsets.UTF_8));
        }
        setDaemon(true);
        setName(name + "Logger");
        start();
    }

    @Override
    public void run() {
        while (running) {
            synchronized (lines) {
                if (lines.isEmpty()) {
                    continue;
                }
                while (!lines.isEmpty()) {
                    String line = lines.remove(0);
                    FileUtil.append(path, line);
                }
            }
        }
    }

    public static void stopAll() {
        running = false;
    }

    /**
     * Enable a log level.
     *
     * @param level the log level to enable
     */
    public static void enable(LogLevel level) {
        level.enabled = true;
    }

    /**
     * Disable a log level.
     *
     * @param level the log level to disable
     */
    public static void disable(LogLevel level) {
        level.enabled = false;
    }

    /**
     * Log levels.<br>
     * Not that {@link LogLevel#DEBUG} and {@link LogLevel#TRACE} are disabled by default, unless property alice.debug is enabled.
     */
    public enum LogLevel {
        FATAL, ERROR, WARN, INFO, DEBUG(false), TRACE(false);
        boolean enabled;

        LogLevel() {
            enabled = true;
        }

        LogLevel(boolean enabled) {
            this.enabled = enabled;
        }

    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void log(LogLevel level, String message) {
        if (!level.enabled) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(LocalDateTime.now().format(formatter)).append(']');
        switch (level) {
            case INFO: {
                sb.append("[INFO]");
                break;
            }
            case WARN: {
                sb.append("[WARN]");
                break;
            }
            case ERROR: {
                sb.append("[ERROR]");
                break;
            }
            case TRACE: {
                sb.append("[TRACE]");
                break;
            }
            case DEBUG: {
                sb.append("[DEBUG]");
                break;
            }
            case FATAL: {
                sb.append("[FATAL]");
                break;
            }

        }
        sb.append(message).append('\n');
        synchronized (lines) {
            lines.add(sb.toString());
        }
    }
}
