package org.server.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.server.config.shared.Controller;
import org.server.config.shared.ServletAutoMapping;

import java.io.IOException;

@ServletAutoMapping("/api/bye")
@Controller
public class ByeServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1> Bye Servlet </h1>");
    }
}
