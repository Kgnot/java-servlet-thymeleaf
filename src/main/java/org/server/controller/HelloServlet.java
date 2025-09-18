package org.server.controller;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.thymeleaf.ThymeleafConfig;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.model.service.UserService;
import org.server.config.shared.ServletAutoMapping;
import org.server.view.render.HomeViewRender;

import java.util.HashMap;


@Controller
@ServletAutoMapping("/api/home")
public class HelloServlet extends HttpServlet {

    @Inject
    private HomeViewRender homeViewRender;
    @Inject
    private UserService userService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/html;charset=UTF-8");
        AsyncContext asyncContext = req.startAsync();
        userService.getUsers()
                .whenComplete((userList, throwable) -> {
                    try {
                        var modelMap = new HashMap<String, Object>();
                        modelMap.put("userList", userList);

                        var templateEngine = ThymeleafConfig.getTemplateEngine();
                        var webExchange = ThymeleafConfig.buildWebExchange(
                                (HttpServletRequest) asyncContext.getRequest(),
                                (HttpServletResponse) asyncContext.getResponse()
                        );

                        homeViewRender.process(
                                webExchange,
                                templateEngine,
                                asyncContext.getResponse().getWriter(),
                                modelMap
                        );

                    } catch (Exception e) {
                        log("Error en GetUsers " + e.getMessage());
                    } finally {
                        asyncContext.complete();
                    }
                });
    }

}

