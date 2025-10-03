package org.server.model.web.handler;

import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.reflections.Reflections;
import org.server.config.beans.ApplicationContext;
import org.server.config.shared.ServletAutoMapping;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@code HandlerServerFactory} se encarga de crear handlers para el servidor Jetty.
 * <p>
 * Proporciona un método para crear un {@link ServletContextHandler} que registra automáticamente
 * los servlets anotados con {@link ServletAutoMapping} y sirve archivos estáticos desde la carpeta "static".
 */
public class HandlerServerFactory {

    /** Logger para mensajes de información y errores. */
    private final static Logger logger;

    static {
        logger = Logger.getLogger(HandlerServerFactory.class.getName());
    }

    /**
     * Crea un {@link ServletContextHandler} con todos los servlets registrados mediante
     * {@link ServletAutoMapping} y un servlet por defecto para servir archivos estáticos.
     *
     * @return un {@link Handler.Abstract} listo para ser agregado al servidor
     */
    public Handler.Abstract createContextServletHandler() {
        ServletContextHandler handler = new ServletContextHandler();

        // Escaneo de servlets con reflection
        Reflections reflections = new Reflections("org.server");
        Set<Class<?>> servlets = reflections.getTypesAnnotatedWith(ServletAutoMapping.class);

        for (var servletClass : servlets) {
            try {
                ServletAutoMapping mapping = servletClass.getAnnotation(ServletAutoMapping.class);
                String path = mapping.value();
                HttpServlet servlet = ApplicationContext.getInstance()
                        .getBean(servletClass.asSubclass(HttpServlet.class));
                if (servlet != null) {
                    handler.addServlet(new ServletHolder(servlet), path);
                } else {
                    logger.warning("No se encontró bean para " + servletClass.getName());
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error al registrar servlet " + servletClass.getName(), e);
            }
        }
//
        return handler;
    }
}
