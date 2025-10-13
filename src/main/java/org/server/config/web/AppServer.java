package org.server.config.web;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.annotation.WebFilter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.ee11.servlet.FilterHolder;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.server.config.beans.ApplicationContext;
import org.server.config.shared.Websocket;

import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

// posiblemente cambiar asi que aja xd

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

                if (handler instanceof ServletContextHandler servletContextHandler) {
                    //registramos los websockets
                    JakartaWebSocketServletContainerInitializer.configure(servletContextHandler,
                            (servletContext, wsContainer) -> {
                                var websocketsBeanList = ApplicationContext.getInstance().getBeansByAnnotation(Websocket.class); // obtenemos todos los Websocket.class
                                for (var websocketBean : websocketsBeanList) {
                                    wsContainer.addEndpoint(websocketBean.getClazz());
                                }
                            });
                    // Debemos registrar los filtros:
                    var filters = ApplicationContext.getInstance().getBeansByAnnotation(WebFilter.class);
                    for (var filterClass : filters) {
                        Filter filterInstance = (Filter) ApplicationContext.getInstance().getBeanByType(filterClass.getClazz());

                        WebFilter filterAnn = filterClass.getClazz().getAnnotation(WebFilter.class);
                        String[] urlPatterns = filterAnn.value().length > 0 ? filterAnn.value() : filterAnn.urlPatterns();

                        for (var url : urlPatterns) {
                            servletContextHandler.addFilter(new FilterHolder(filterInstance),
                                    url,
                                    EnumSet.of(DispatcherType.REQUEST));
                        }

                    }

                }
            }
        } catch (Exception e) {
            log.info("Error creating server " + e);
        }
    }

}
