package org.server.controller.user;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.http.status.HttpStatus;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.config.http.ResponseCommon;
import org.server.model.CQRS.command.impl.user.CreateUserCommand;
import org.server.model.CQRS.command.impl.user.CreateUserCommandHandler;
import org.server.model.CQRS.command.result.CommandResult;
import org.server.model.CQRS.quey.impl.user.GetUserListHandler;
import org.server.model.CQRS.quey.impl.user.GetUserListQuery;
import org.server.config.shared.ServletAutoMapping;

import java.io.BufferedReader;
import java.io.IOException;


@Controller
@ServletAutoMapping("/api/users") // api es para volver a "autenticar"
public class UserServlet extends HttpServlet {

    @Inject
    private GetUserListHandler handler;
    @Inject
    private CreateUserCommandHandler createUserHandler;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GetUserListQuery query = new GetUserListQuery();
        resp.getWriter()
                .write(ResponseCommon.ok(handler.handle(query)).toJson());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            CreateUserCommand command = new Gson().fromJson(sb.toString(), CreateUserCommand.class);
            CommandResult result = createUserHandler.handle(command);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(ResponseCommon.ok(result)));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(new Gson().toJson(ResponseCommon.warning(HttpStatus.BAD_REQUEST, "Error al crear el usuario: " + e.getMessage())));
        }
    }


}

