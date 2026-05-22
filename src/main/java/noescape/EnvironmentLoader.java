package noescape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads key-value configuration from a {@code .env} file at the given path.
 * Falls back to caller-supplied defaults when a key is absent or the file
 * cannot be read.
 *
 * OOP: Encapsulation — file I/O and parse logic are hidden inside this class.
 */
public class EnvironmentLoader {

    private final Map<String, String> configValues = new HashMap<>();

    public EnvironmentLoader(String filePath) {
        loadConfigFile(filePath);
    }

    private void loadConfigFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String rawLine;
            while ((rawLine = reader.readLine()) != null) {
                String trimmedLine = rawLine.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) continue;

                int equalsIndex = trimmedLine.indexOf('=');
                if (equalsIndex > 0) {
                    String key   = trimmedLine.substring(0, equalsIndex).trim();
                    String value = trimmedLine.substring(equalsIndex + 1).trim();
                    configValues.put(key, value);
                }
            }
        } catch (Exception exception) {
            System.out.println("Warning: .env not found or unreadable. Using default values.");
        }
    }

    public String get(String key, String defaultValue) {
        return configValues.getOrDefault(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(configValues.getOrDefault(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = configValues.get(key);
        if (value == null) return defaultValue;
        return value.equalsIgnoreCase("true");
    }
}