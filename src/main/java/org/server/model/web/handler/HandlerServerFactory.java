package org.server.model.web.handler;

import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.reflections.Reflections;
import org.server.utils.ServletAutoMapping;

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

                Object servlet = servletClass.getDeclaredConstructor().newInstance();
                handler.addServlet(new ServletHolder((HttpServlet) servlet), path);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al crear el servlet contexto", e);
            }
        }
// Static Files:
        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase",
                Objects.requireNonNull(getClass().getClassLoader().getResource("static")).toExternalForm());
        defaultServlet.setInitParameter("dirAllowed", "true");
        handler.addServlet(defaultServlet, "/static/*");

        return handler;
    }

    public Handler.Abstract createResourceHandler() {
        return new ResourceHandler();
    }


}
