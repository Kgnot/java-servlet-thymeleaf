package org.server.view;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public interface IGTVGController {

    void process(
            final IWebExchange webExchange,
            final ITemplateEngine templateEngine,
            final Writer writer)
            throws Exception;

}