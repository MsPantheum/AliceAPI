package alice._native;

import alice.Platform;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class KernelConfig {
    private static final Set<String> builtin = new HashSet<>();
    private static final Set<String> module = new HashSet<>();

    static {
        try {
            if (!Platform.win32) {
                Path p = Paths.get("/proc/config.gz");
                GZIPInputStream gis = new GZIPInputStream(Files.newInputStream(p));
                InputStreamReader inputStreamReader = new InputStreamReader(gis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.isEmpty() || line.charAt(0) == '#') {
                        continue;
                    }
                    String[] split = line.split("=");
                    if (split[1].equals("y")) {
                        builtin.add(split[0]);
                    } else {
                        module.add(split[0]);
                    }
                }
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isConfigBuiltin(String CONFIG) {
        return builtin.contains(CONFIG);
    }

    public static boolean isConfigModule(String CONFIG) {
        return module.contains(CONFIG);
    }

    public static boolean hasConfig(String CONFIG) {
        return isConfigBuiltin(CONFIG) || isConfigModule(CONFIG);
    }
}
