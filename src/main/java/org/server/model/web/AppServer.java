package org.server.model.web;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.server.config.beans.ApplicationContext;
import org.server.config.shared.Websocket;

import java.util.List;
import java.util.logging.Logger;

public class AppServer {
    private static final Logger log;

    @Getter
    private final Server server;
    @Setter
    private Connector connector;
    @Setter
    private List<Handler.Abstract> handler;

    static {
        log = Logger.getLogger(AppServer.class.getName());
        log.info("Starting ServerWeb ...");
    }

    public AppServer() {
        server = new Server();
    }

    public void createServer() {
        server.setStopAtShutdown(true);
        server.addConnector(connector);
        try {
            for (Handler.Abstract handler : handler) {
                server.setHandler(handler);
                if (handler instanceof ServletContextHandler) {
                    JakartaWebSocketServletContainerInitializer.configure((ServletContextHandler) handler,
                            (servletContext, wsContainer) -> {
                                var websockets = ApplicationContext.getInstance().getBeansByAnnotation(Websocket.class); // obtenemos todos los Websocket.class
                                for (var websocket : websockets) {
                                    wsContainer.addEndpoint(websocket);
                                }
                            });
                }
            }
        } catch (Exception e) {
            log.info("Error creating server " + e);
        }
    }

}
