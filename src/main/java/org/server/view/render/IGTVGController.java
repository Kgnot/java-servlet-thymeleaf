package org.server.view.render;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
import java.util.Map;

public interface IGTVGController {

    void process(
            final IWebExchange webExchange,
            final ITemplateEngine templateEngine,
            final Writer writer,
            final Map<String, Object> variables) throws Exception;

}