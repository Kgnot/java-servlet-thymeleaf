package org.server;

import org.server.config.beans.ApplicationContext;
import org.server.config.web.AppServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext.getInstance().initialize();
        AppServerService appServer = new AppServerService();

        Thread serverThread = new Thread(() -> {
            try {
                appServer.start();
            } catch (Exception e) {
                logger.error("Error starting server", e);
            }
        });

        serverThread.start();
        serverThread.join();
    }

}
