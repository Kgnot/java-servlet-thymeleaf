package org.server.controller.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.http.ResponseCommon;
import org.server.model.CQRS.quey.impl.user.GetUserListHandler;
import org.server.model.CQRS.quey.impl.user.GetUserListQuery;
import org.server.config.shared.ServletAutoMapping;

import java.io.IOException;


@Controller
@ServletAutoMapping("/api/users")
public class UserServlet extends HttpServlet {

    @Inject
    private GetUserListHandler handler;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GetUserListQuery query = new GetUserListQuery();
        resp.getWriter()
                .write(ResponseCommon.ok(handler.handle(query)).toJson());
    }

}

