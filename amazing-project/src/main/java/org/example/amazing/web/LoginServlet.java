package org.example.amazing.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.amazing.core.Role;
import org.example.amazing.db.UserDAO;
import org.example.amazing.db.UserDTO;
import org.example.amazing.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDAO userDAO = new UserDAO();
        try {
            UserDTO user = userDAO.fetchUserByUsername(req.getParameter("username"));
            byte[] salt = user.getSalt();
            byte[] hashedPassword = user.getHashedPassword();
            boolean outcome = UserService.login(req.getParameter("password"), salt, hashedPassword);
            if (outcome) {
                req.getSession().setAttribute("auth_token", Role.USER);
                resp.sendRedirect("homepage");
            }
            resp.sendRedirect("login");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
