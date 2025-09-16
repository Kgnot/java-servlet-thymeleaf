package org.server.web;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.VirtualThreadPool;

import java.util.List;
import java.util.logging.Logger;

public class AppServer {

    private static final Logger log;
    private final Server server;
    private Connector connector;
    private List<Handler.Abstract> handler;


    private ServletContextHandler context;

    static {
        log = Logger.getLogger(AppServer.class.getName());
        log.info("Starting ServerWeb ...");
    }


    public AppServer() {
        server = new Server();
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public void setHandler(List<Handler.Abstract> handler) {
        this.handler = handler;
    }

    public Server getServer() {
        return server;
    }

//

    public void createServer() {
        server.setStopAtShutdown(true);
        server.addConnector(connector);
        try {
            for (Handler.Abstract handler : handler) {
                server.setHandler(handler);
            }
        } catch (Exception e) {
            log.info("Error creating server " + e);
        }
    }
}
