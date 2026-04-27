package alice.log;

import alice.LaunchWrapper;
import alice.util.FileUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A simple logger.
 */
public class Logger extends Thread {

    private static final boolean IMMEDIATELY_LOG = "true".equals(System.getProperty("alice.debug.log.log_immediately"));

    public static final Logger MAIN;

    private static final List<Logger> ALL_LOGGERS;

    static {
        ALL_LOGGERS = new ArrayList<>();
        MAIN = new Logger("Alice");
        if ("true".equals(System.getProperty("alice.debug"))) {
            Logger.enable(Logger.LogLevel.DEBUG);
            Logger.enable(Logger.LogLevel.TRACE);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> ALL_LOGGERS.forEach(Logger::flush)));
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path path;
    private final ArrayBlockingQueue<String> lines = new ArrayBlockingQueue<>(1024);

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
        if (!"true".equals(System.getProperty("alice.debug.prioritize_logger"))) {
            setPriority(Thread.MIN_PRIORITY);
        }
        start();
        ALL_LOGGERS.add(this);
    }

    @Override
    public void run() {
        while (LaunchWrapper.running) {
            if (lines.isEmpty()) {
                continue;
            }
            flush();
        }
    }

    private void flush() {
        while (!lines.isEmpty()) {
            String line = lines.poll();
            FileUtil.append(path, line);
        }
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


        @Override
        public String toString() {
            switch (this) {
                case INFO:
                    return "info";
                case FATAL:
                    return "fatal";
                case TRACE:
                    return "trace";
                case WARN:
                    return "warn";
                case ERROR:
                    return "error";
                case DEBUG:
                    return "debug";
                default:
                    throw new IllegalStateException();
            }
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

    public void printStackTrace() {
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            trace(ste.toString());
        }
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
        if (!IMMEDIATELY_LOG) {
            lines.offer(sb.toString());
        } else {
            FileUtil.append(path, sb.toString());
        }
    }
}
