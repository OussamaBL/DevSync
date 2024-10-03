/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.User;
import Service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "users",urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize UserServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            List<User> userList = userService.findAllUsers();
            System.out.println(userList);
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("ListUser.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            User user = userService.findUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("FormUser.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        String id = request.getParameter("id");
        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                userService.deleteUser(Long.parseLong(id));
            }
        } else {
            String userName = request.getParameter("userName");
            String name = request.getParameter("name");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = new User(0L,userName,password,name,lastName, email,true);
            if (id != null && !id.isEmpty()) {
                user.setId(Long.parseLong(id));
                userService.updateUser(user);
            } else {
                userService.insertUser(user);
            }
        }
        response.sendRedirect("users?status=success");
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("users");
    }
}