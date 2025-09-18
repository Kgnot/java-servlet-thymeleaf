package org.server.config.thymeleaf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class ThymeleafConfig {

    private static final TemplateEngine engine;

    static {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        // CSS:


        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    public static TemplateEngine getTemplateEngine() {
        return engine;
    }

    public static org.thymeleaf.web.IWebExchange buildWebExchange(HttpServletRequest req, HttpServletResponse resp) {
        var application = JakartaServletWebApplication.buildApplication(req.getServletContext());
        return application.buildExchange(req, resp);
    }
}
