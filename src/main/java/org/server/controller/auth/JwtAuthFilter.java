package org.server.controller.auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Component;

import java.io.IOException;

@Component
@WebFilter("/api/*")
public class JwtAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Aqui excluimos, después haremos esto mucho mejor xd
        if (path.equals("/api/login") || path.equals("api/logout")) {
            chain.doFilter(request, response);
            return;
        }


        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String token = authHeader.substring(7);

        if (!JwtService.validateToken(token)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"Token inválido o revocado\"}"); // esto ahora lo cambio
            return;
        }

        // Esto significa que el toquen ha sido válido
        chain.doFilter(request, response);
    }
}
