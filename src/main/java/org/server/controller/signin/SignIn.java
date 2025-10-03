package org.server.controller.signin;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.ServletAutoMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@ServletAutoMapping("/api/login")
public class SignIn extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Leer el JSON del body
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        ModeloLogin login = gson.fromJson(reader, ModeloLogin.class);
        // elegimos la respuesta
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.println("{\"success\":\"Usuario " + login.name() + " registrado\"}");
    }


}
