package noescape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
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
                    String key = trimmedLine.substring(0, equalsIndex).trim();
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

    public Escapable[] buildRoomSequence(String course) {
        String prefix = course.contains("Nursing") ? "NR_" : "CS_";

        return new Escapable[]{
            new Classroom(
                configValues.get(prefix + "ROOM1_NAME"),
                false,
                configValues.get(prefix + "ROOM1_PUZZLE"),
                configValues.get(prefix + "ROOM1_ANSWER"),
                configValues.get(prefix + "ROOM1_CLUE"),
                configValues.get(prefix + "ROOM1_HINT")
            ),
            new LibraryRoom(
                configValues.get(prefix + "ROOM2_NAME"),
                true,
                configValues.get(prefix + "ROOM2_PUZZLE"),
                configValues.get(prefix + "ROOM2_ANSWER"),
                configValues.get(prefix + "ROOM2_CLUE"),
                configValues.get(prefix + "ROOM2_HINT")
            ),
            new TsgRoom(
                configValues.get(prefix + "ROOM3_NAME"),
                true,
                configValues.get(prefix + "ROOM3_PUZZLE"),
                configValues.get(prefix + "ROOM3_ANSWER"),
                configValues.get(prefix + "ROOM3_CLUE"),
                configValues.get(prefix + "ROOM3_HINT")
            ),
            new SecurityOfficeRoom(
                configValues.get(prefix + "ROOM4_NAME"),
                true,
                configValues.get(prefix + "ROOM4_PUZZLE"),
                configValues.get(prefix + "ROOM4_ANSWER"),
                configValues.get(prefix + "ROOM4_CLUE"),
                configValues.get(prefix + "ROOM4_HINT")
            )
        };
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