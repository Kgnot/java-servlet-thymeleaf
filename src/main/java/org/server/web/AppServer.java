package org.server.web;

import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.VirtualThreadPool;
import org.server.controller.ByeServlet;
import org.server.controller.HelloServlet;

import java.util.logging.Logger;

public class AppServer {

    private static final Logger log;
    private final AppServletConfigHandler configHandler;

    static {
        log = Logger.getLogger(AppServer.class.getName());
        log.info("Starting ServerWeb ...");
    }

    private final Server server;

    public AppServer() {
        this(8080);
    }

    public AppServer(int port) {
        configHandler = AppServletConfigHandler.getInstance();
        server = createServer(port);
    }


    // private class
    private Server createServer(int port) {
        var server = new Server(new VirtualThreadPool());
        server.setStopAtShutdown(true);
        server.addConnector(createConnector(port, server));
        try {
            assert configHandler != null;
            server.setHandler(configHandler.getContextHandler());
        } catch (Exception e) {
            log.info("Error creating server " + e);
        }
        return server;
    }

    //
    private HelloServlet createHelloServlet() {
        return new HelloServlet();
    }

    private ByeServlet createByeServlet() {
        return new ByeServlet();
    }

    //
    private ServerConnector createConnector(int port, Server server) {
        var connector = new ServerConnector(server);
        connector.setPort(port);
        connector.setName("main");
        return connector;
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

}
