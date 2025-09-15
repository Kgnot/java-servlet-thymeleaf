package org.server.view;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public class HomeView implements IGTVGController {
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {

        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("title", "Página de Inicio");
        ctx.setVariable("username", "Yo xd");
        ctx.setVariable("numbers", new int[]{1, 2, 3, 4, 5});

        templateEngine.process("home", ctx, writer);

    }
}
