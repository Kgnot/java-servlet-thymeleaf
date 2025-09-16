package org.server;

import org.server.web.AppServer;
import org.server.web.AppServerService;


public class Main {
    public static void main(String[] args) {

        // iniciamos el server:
        AppServerService appServer = new AppServerService();
        try {
            appServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
