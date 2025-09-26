package org.server.controller;

import com.google.gson.Gson;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.model.service.UserService;
import org.server.config.shared.ServletAutoMapping;
import java.io.IOException;


@Controller
@ServletAutoMapping("/api/home")
public class HelloServlet extends HttpServlet {


    @Inject
    private UserService userService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        AsyncContext asyncContext = req.startAsync();
        userService.getUsers()
                .whenComplete((userList, throwable) -> {
                    String json = new Gson().toJson(userList);
                    try {
                        asyncContext.getResponse().getWriter().write(json);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        asyncContext.complete();
                    }
                });
    }

}

