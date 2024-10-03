package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/devSync2/*")
public class MainServlet extends HomeServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/users":
                request.getRequestDispatcher("/users").forward(request, response); // Forward to UserServlet
                break;
            case "/home":
                request.getRequestDispatcher("/home").forward(request, response); // Forward to HomeServlet
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404 if action doesn't match
                break;
        }
    }
}