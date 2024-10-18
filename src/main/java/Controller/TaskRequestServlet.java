package Controller;

import Exceptions.TaskExistException;
import Exceptions.UserNotExistException;
import Model.Enums.UserType;
import Model.Task;
import Model.TaskRequest;
import Model.User;
import Service.TaskRequestService;
import Service.TaskService;
import Service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "requestTask",urlPatterns = {"/requestTask"})
public class TaskRequestServlet extends HttpServlet {

    private TaskRequestService taskRequestService;
    private TaskService taskService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            taskRequestService = new TaskRequestService();
            taskService = new TaskService();
            userService = new UserService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize RequestServlet "+ e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if (action.equals("list")) {
                User user = (User) request.getSession().getAttribute("user");
                List<TaskRequest> listRequest = new ArrayList<>();
                if (user.getRole_user() == UserType.MANAGER)
                    listRequest = taskRequestService.getAllRequests();
                else listRequest = taskRequestService.getRequestbyUser(user.getId());

                request.setAttribute("listRequest", listRequest);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("ListRequest.jsp");
                requestDispatcher.forward(request, response);

            }
        }
        catch (Exception e) {
            response.sendRedirect("tasks?action=list&status=" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            Long userId = Long.parseLong(request.getParameter("user_id"));
            String requestType = request.getParameter("requestType");

            Optional<Task> task= taskService.getTaskById(taskId);
            User user = userService.findUserById(userId);

            if (task.isEmpty()) {
                throw new TaskExistException("task not found with id: " + taskId);
            }

            if (user == null) {
                throw new UserNotExistException("user not found with id: " + userId);
            }

            TaskRequest taskRequest = new TaskRequest();
            taskRequest.setTask(task.get());
            taskRequest.setUser(user);

            //request for change the assigne user (from user)
            if ("REJECTED".equalsIgnoreCase(requestType)) {
                Integer dailyTokens = (Integer) request.getSession().getAttribute("dailyTokens");
                if (dailyTokens != null && dailyTokens > 0) {
                    taskRequestService.saveRequest(taskRequest);
                    request.getSession().setAttribute("dailyTokens", dailyTokens - 1);
                    userService.updateUserTokens(userId, dailyTokens - 1, user.getMonthlyToken());
                } else {
                    response.sendRedirect("tasks?action=list&status=dailytokens_not_found");
                    return;
                }
            }
            response.sendRedirect("tasks?action=list&status=success");
        }
        catch (Exception e){
            response.sendRedirect("tasks?action=list&status="+e.getMessage());
        }

    }


}
