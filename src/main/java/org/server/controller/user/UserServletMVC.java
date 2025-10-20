package org.server.controller.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.config.shared.ServletAutoMapping;
import org.server.config.http.ResponseCommon;
import org.server.config.thymeleaf.ThymeleafConfig;
import org.server.model.CQRS.quey.impl.user.GetUserListHandler;
import org.server.model.CQRS.quey.impl.user.GetUserListQuery;
import org.server.model.dto.UserDto;
import org.server.view.render.HomeViewRender;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@ServletAutoMapping("/mvc/users")
public class UserServletMVC extends HttpServlet {

    @Inject
    private HomeViewRender homeViewRender;
    @Inject
    private GetUserListHandler handler;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        GetUserListQuery query = new GetUserListQuery();
        List<UserDto> userList = handler.handle(query);
        // modelamos:
        var modelMap = new HashMap<String, Object>();
        modelMap.put("userList", userList);
        // Apartado para el render:
        this.rendered(req, resp, modelMap);

    }

    private void rendered(HttpServletRequest req, HttpServletResponse resp, HashMap<String, Object> modelMap) throws IOException {
        var templateEngine = ThymeleafConfig.getTemplateEngine();
        var webExchange = ThymeleafConfig
                .buildWebExchange(req, resp);
        homeViewRender.process(
                webExchange,
                templateEngine,
                resp.getWriter(),
                modelMap
        );
    }

}
