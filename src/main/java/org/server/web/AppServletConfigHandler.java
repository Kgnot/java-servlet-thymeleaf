package org.server.web;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.server.controller.ByeServlet;
import org.server.controller.HelloServlet;

import java.util.Objects;

public class AppServletConfigHandler {

    private final ServletContextHandler contextHandler;
    private static AppServletConfigHandler instance;

    private AppServletConfigHandler() {
        contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        contextHandler.addServlet(new ServletHolder(new HelloServlet()), "/api/home");
        contextHandler.addServlet(new ServletHolder(new ByeServlet()), "/api/bye");

        // recursos estaticos
        getStaticResources();
    }

    private void getStaticResources() {
        var defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase",
                Objects.requireNonNull(AppServletConfigHandler.class.getClassLoader().getResource("static")).toExternalForm());
        defaultServlet.setInitParameter("dirAllowed", "true"); // opcional: permite listar directorios
        contextHandler.addServlet(defaultServlet, "/static/*");
    }


    public static AppServletConfigHandler getInstance() {
        if (instance == null) {
            instance = new AppServletConfigHandler();
        }
        return instance;
    }

    public ServletContextHandler getContextHandler() {
        return contextHandler;
    }

}
