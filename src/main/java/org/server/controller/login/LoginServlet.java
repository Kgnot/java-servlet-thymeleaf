package org.server.controller.login;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.ServletAutoMapping;
import org.server.controller.auth.JwtService;

import java.io.BufferedReader;
import java.io.IOException;

@Controller
@ServletAutoMapping("/api/login")
public class LoginServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        // Usando GSON:
        Gson gson = new Gson();
        BufferedReader br = req.getReader();
        ModeloLogin login = gson.fromJson(br, ModeloLogin.class);
        String username = login.username();
        String password = login.password();


        // aquí iria el servicio de los usuarios para verificar la contraseña, una vez verificada:
        if ("admin".equals(username) && "1234".equals(password)) {
            String token = JwtService.generateToken(username);
            res.setContentType("application/json");
            res.getWriter().write("{\"token\":\"" + token + "\"}"); // esto se debe cambiar xd
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"Credenciales inválidas\"}");
        }
    }


}
