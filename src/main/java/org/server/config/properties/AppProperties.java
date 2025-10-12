package org.server.config.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = AppProperties.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                throw new RuntimeException("No se encontró application.properties en resources/");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo application.properties", e);
        }
    }

    public static String get(String key) {
        // Prioridad: variable de entorno > properties
        String value = System.getenv(key);
        if (value == null) value = props.getProperty(key);
        return value;
    }

}
