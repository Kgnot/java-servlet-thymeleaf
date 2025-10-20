package org.server.controller.user;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.http.ResponseCommon;
import org.server.config.http.status.HttpStatus;
import org.server.config.shared.Controller;
import org.server.config.shared.Inject;
import org.server.config.shared.ServletAutoMapping;
import org.server.config.http.CommonError;
import org.server.model.service.UserService;

import java.io.IOException;

@Controller
@ServletAutoMapping("/api/users/*")
public class UserByIdServlet extends HttpServlet {

    @Inject
    UserService userService;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        AsyncContext context = req.startAsync();
        int id = 2;

        userService.getUser(id).whenComplete(
                (user, throwable) -> {
                    try {
                        if (throwable != null) {
                            resp.setStatus(500);
                            CommonError error = CommonError
                                    .of(HttpStatus.INTERNAL_SERVER_ERROR,
                                            "Error al buscar un usuario por id");
                            context.getResponse()
                                    .getWriter()
                                    .write(error.toJson());
                            return;
                        }
                        if (user == null) {
                            CommonError error = CommonError
                                    .of(HttpStatus.NOT_FOUND,
                                            "Usuario no encontrado");
                            context.getResponse()
                                    .getWriter()
                                    .write(error.toJson());
                            return;
                        }

                        context.getResponse()
                                .getWriter()
                                .write(ResponseCommon.ok(user).toJson());

                    } catch (IOException e) {
                        log("Error escribiendo respuesta: " + e.getMessage());
                    } finally {
                        context.complete();
                    }
                }
        );
    }
}
