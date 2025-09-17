package org.server.model.web.handler;

import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee11.servlet.DefaultServlet;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.reflections.Reflections;
import org.server.config.AppConfig;
import org.server.config.shared.ServletAutoMapping;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlerServerFactory {

    private final static Logger logger;

    static {
        logger = Logger.getLogger(HandlerServerFactory.class.getName());
    }

    public Handler.Abstract createContextServletHandler() {
        ServletContextHandler handler = new ServletContextHandler();

        // usamos reflection
        Reflections reflections = new Reflections("org.server");
        Set<Class<?>> servlets = reflections.getTypesAnnotatedWith(ServletAutoMapping.class);

        for (var servletClass : servlets) {
            try {
                ServletAutoMapping mapping = servletClass.getAnnotation(ServletAutoMapping.class);
                String path = mapping.value();
                HttpServlet servlet = AppConfig.getBean(servletClass.asSubclass(HttpServlet.class));
                if (servlet != null) {
                    handler.addServlet(new ServletHolder(servlet), path);
                } else {
                    logger.warning("No se encontró bean para " + servletClass.getName());
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al registrar servlet " + servletClass.getName(), e);
            }
        }

        // Static Files:
        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter(
                "resourceBase",
                Objects.requireNonNull(getClass().getClassLoader().getResource("static")).toExternalForm()
        );
        defaultServlet.setInitParameter("dirAllowed", "true");
        handler.addServlet(defaultServlet, "/static/*");

        return handler;
    }

}
