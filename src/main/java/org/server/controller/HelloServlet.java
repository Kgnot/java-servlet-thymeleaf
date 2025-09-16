package org.server.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.ThymeleafConfig;
import org.server.utils.ServletAutoMapping;
import org.server.view.HomeView;

import java.io.IOException;


@ServletAutoMapping("/api/home")
public class HelloServlet extends HttpServlet {

    private final HomeView homeView = new HomeView();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            var templateEngine = ThymeleafConfig.getTemplateEngine();
            var webExchange = ThymeleafConfig.buildWebExchange(req, resp);
            homeView.process(webExchange, templateEngine, resp.getWriter());

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error procesando template: " + e.getMessage());
        }
    }
}

