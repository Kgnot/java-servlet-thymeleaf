package org.server.model.web;

import org.server.model.web.connector.ConnectorFactory;
import org.server.model.web.handler.HandlerServerFactory;

import java.util.List;

public class AppServerService {

    private final AppServer appServer;
    private final int port = 8080;

    public AppServerService() {
        appServer = new AppServer();
        HandlerServerFactory handlerFactory = new HandlerServerFactory();
        ConnectorFactory connectorFactory = new ConnectorFactory(appServer.getServer());
        //setters
        appServer.setConnector(
                connectorFactory.createHttpConnector(port)); // Crear la variable global en properties
        appServer.setHandler(
                List.of(handlerFactory.createContextServletHandler())
        );
        // create server

        appServer.createServer();
    }

    public void start() throws Exception {
        this.appServer.getServer().start();
    }

    public void stop() throws Exception {
        this.appServer.getServer().stop();
    }
}
