package Controller;

import Exceptions.FormatIncorrectException;
import Exceptions.RequestValidationException;
import Exceptions.TaskExistException;
import Exceptions.UserNotExistException;
import Model.Enums.StatusRequest;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "taskRequest_manager",urlPatterns = {"/taskRequest_manager"})
public class ManagerRequestTask_Servlet extends HttpServlet {
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
            throw new ServletException("Failed to initialize RequestServlet", e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("ACCEPT")) {
            handleAcceptRedirect(request, response);
        }
    }

    //accept assignation
    private void handleAcceptRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            Optional<TaskRequest> requestToProcess = taskRequestService.getRequestById(requestId);

            if (requestToProcess.isPresent()) {
                TaskRequest req = requestToProcess.get();
                Task task = req.getTask();
                request.setAttribute("requestId", requestId);

                if (task == null || task.equals(new Task()))
                    throw new TaskExistException("No task associated with the request.");

                Long userRequestId = (long) req.getUser().getId();

                // Update the request status to accepted
                taskRequestService.updateRequestStatus(requestId, StatusRequest.ACCEPTED);

                request.setAttribute("taskTitle", task.getTitle());
                request.setAttribute("taskDescription", task.getDescription());
                request.setAttribute("taskId", task.getId());
                request.setAttribute("requestUserId", userRequestId);

                List<User> users = userService.findAllUsers();
                List<User> filteredUsers = users.stream()
                        .filter(user -> user.getId() != userRequestId).filter(user -> user.getRole_user() == UserType.USER)
                        .collect(Collectors.toList());

                request.setAttribute("users", filteredUsers);

                RequestDispatcher requestDispatcher = request.getRequestDispatcher("taskAssigne.jsp");
                requestDispatcher.forward(request, response);

            } else throw new RequestValidationException("Request not exist");

        }
        catch (Exception e){
            response.sendRedirect("requestTask?action=list&status="+e.getMessage());
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");
        if (requestIdParam == null || requestIdParam.isEmpty())
            throw new FormatIncorrectException("request id is null ");

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdParam);
        } catch (NumberFormatException e) {
            throw new FormatIncorrectException("Invalid Request ID format.");
        }

        Optional<TaskRequest> requestToProcess = taskRequestService.getRequestById(requestId);

        if (requestToProcess.isEmpty())
             throw new RequestValidationException("Request not found");

        String action = request.getParameter("action");
        if (action == null)
            throw new FormatIncorrectException("Action is required");


        switch (action.toUpperCase()) {
            case "ACCEPT":
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                Long assignedUserId = Long.parseLong(request.getParameter("newAssignedUser"));
                processAcceptRequest(taskId, assignedUserId);
                break;
                //refuse request of user
            case "DELETE":
                processDeclineRequest(requestToProcess);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
        }

        // Redirect back to the request list
        response.sendRedirect("requestTask?action=list&status=success");
    }

    private void processDeclineRequest(Optional<TaskRequest> request) {
        if (!request.isPresent()) throw new RequestValidationException("Request is not present.");

        TaskRequest req = request.get();
        Task task = req.getTask();
        User user = req.getUser();
        if (user == null) throw new UserNotExistException("User associated with the request is null.");
        if (task == null) throw new TaskExistException("Task associated with the request is null.");
        processDailyToken(user);
        taskRequestService.updateRequestStatus(req.getId(), StatusRequest.REJECTED);
    }

    private void processDailyToken(User user) {
        if (user.getDailyToken() == 0) {
            userService.updateDailyTokens((long) user.getId(), user.getDailyToken() + 1);
        }
        userService.updateDailyTokens((long) user.getId(), user.getDailyToken() * 2);
    }

    private void processAcceptRequest(int taskId, Long assignedUserId) {
        Optional<Task> t = taskService.getTaskById(taskId);
        if (t.isEmpty()) throw new TaskExistException("Task not found.");
        User assignedUser = userService.findUserById(assignedUserId);
        if (assignedUser == null) throw new UserNotExistException("Assigned user not found.");

        // update the task with new assigned user and mark it as refused
        Task task=t.get();
        task.setUser_assigne(assignedUser);
        task.setIsRefused(true);
        taskService.updateTask(task);
    }


}
