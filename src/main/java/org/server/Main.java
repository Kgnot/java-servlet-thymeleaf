package org.server;

import org.server.config.ApplicationContext;
import org.server.model.web.AppServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Properties:
        readProperties();
        ApplicationContext.getInstance().initialize(); // inicializamos
        // iniciamos el server:
        AppServerService appServer = new AppServerService();
        try {
            appServer.start();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void readProperties() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
            for (String name : properties.stringPropertyNames()) {
                System.setProperty(name, properties.getProperty(name));
            }
        } catch (IOException e) {
            log.error("Error al abrir config.properties", e);
        }
    }
}
