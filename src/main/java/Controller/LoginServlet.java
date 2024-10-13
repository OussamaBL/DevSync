package Controller;

import Model.Enums.TaskStatus;
import Model.Tag;
import Model.Task;
import Model.User;
import Service.TaskService;
import Service.UserService;
import Utils.SessionUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "login",urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.isUserLoggedIn(req, resp)) {
            resp.sendRedirect(req.getContextPath() + "/tasks");
            return;
        }
        req.getRequestDispatcher("Login.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String method = req.getParameter("_method");
        if (method.equals("DESTROY")) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath()+"/login");
        } else {

            String email = req.getParameter("email");
            String password = req.getParameter("password");

            User user = authenticate(email, password);
            if (user != null) {
                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("dailyTokens", user.getDailyToken());
                req.getSession().setAttribute("monthlyTokens", user.getMonthlyToken());
                resp.sendRedirect("tasks?action=list");
            } else {
                req.setAttribute("error", "Invalid username or password");
                RequestDispatcher dispatcher = req.getRequestDispatcher("Login.jsp");
                dispatcher.forward(req, resp);
            }
        }
    }

    public User authenticate(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
