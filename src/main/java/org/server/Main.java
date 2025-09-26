package org.server;

import org.server.config.beans.ApplicationContext;
import org.server.model.web.AppServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext.getInstance().initialize(); // inicializamos los beans
        AppServerService appServer = new AppServerService();
        try {
            appServer.start();
        } catch (Exception e) {
            log.error("Error al iniciar la app", e);
        }
    }
}
