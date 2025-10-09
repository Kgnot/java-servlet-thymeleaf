package org.server.controller.filter;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Component;
import org.server.controller.signIn.ModeloSignIn;

import java.io.BufferedReader;
import java.io.IOException;

//Especificamos la ruta :p
@Component
@WebFilter("/api/sign-in")
public class AgeValidationFilter implements Filter {

    // y lo que debe hacer el filtro
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Solo interceptamos POST con JSON
        if ("POST".equalsIgnoreCase(req.getMethod()) && req.getContentType().contains("application/json")) {
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();
            ModeloSignIn user = gson.fromJson(reader, ModeloSignIn.class);

            if (user.age() < 18) {
                res.setContentType("application/json");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("{\"error\":\"El usuario debe ser mayor de edad\"}");
                return; // detenemos la cadena, no llega al servlet
            }
        }

        // pasa al siguiente filtro o al servlet
        chain.doFilter(request, response);
    }
}