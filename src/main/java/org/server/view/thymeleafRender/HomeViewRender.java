package org.server.view.thymeleafRender;

import org.server.config.shared.ViewRender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
import java.util.Map;


@ViewRender
public class HomeViewRender implements IGTVGController {
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, Map<String, Object> model) {

        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        for (var entry : model.entrySet()) {
            ctx.setVariable(entry.getKey(), entry.getValue());
        }
        ctx.setVariable("title", "Página de Inicio");
        ctx.setVariable("username", "Yo xd");
        ctx.setVariable("userList", model.get("userList")); // pensar esto mejor

        templateEngine.process("home", ctx, writer);
    }

}
