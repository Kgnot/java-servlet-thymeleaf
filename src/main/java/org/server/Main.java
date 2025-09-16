package org.server;

import org.server.model.web.AppServerService;


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
