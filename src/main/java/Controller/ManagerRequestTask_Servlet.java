package Controller;

import Model.Enums.StatusRequest;
import Model.Enums.TypeRequest;
import Model.Task;
import Model.TaskRequest;
import Model.User;
import Service.TaskRequestService;
import Service.TaskService;
import Service.UserService;

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
        /*else {
            List<TaskRequest> requestList = taskRequestService.getAllRequests();
            request.setAttribute("requests", requestList);
            request.getRequestDispatcher("/views/dashboard/manager/requests/home.jsp").forward(request, response);
        }*/
    }

    //accept assignation
    private void handleAcceptRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID format.");
            return;
        }

        Optional<TaskRequest> requestToProcess = taskRequestService.getRequestById(requestId);

        if (requestToProcess.isPresent()) {
            TaskRequest req = requestToProcess.get();
            Task task = req.getTask();

            // Ensure you set the requestId attribute here
            request.setAttribute("requestId", requestId); // Set the request ID attribute

            if (task == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No task associated with the request.");
                return;
            }

            Long userRequestId = (long) req.getUser().getId();

            // Update the request status to approved
            taskRequestService.updateRequestStatus(requestId, StatusRequest.ACCEPTED); // Update the request status to APPROVED

            request.setAttribute("taskTitle", req.getTask().getTitle());
            request.setAttribute("taskDescription", task.getDescription());
            request.setAttribute("taskId", req.getTask().getId());
            request.setAttribute("requestUserId", req.getUser().getId());
            List<User> users = userService.findAllUsers();
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getId() != userRequestId)
                    .collect(Collectors.toList());

            request.setAttribute("users", filteredUsers);

            request.getRequestDispatcher("taskAssigne.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");
        System.out.println("Received requestIdParam: " + requestIdParam); // Debugging line

        if (requestIdParam == null || requestIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required.");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID format.");
            return;
        }

        Optional<TaskRequest> requestToProcess = taskRequestService.getRequestById(requestId);

        if (requestToProcess.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required.");
            return;
        }

        switch (action.toUpperCase()) {
            case "ACCEPT":
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                Long assignedUserId = Long.parseLong(request.getParameter("newAssignedUser")); // Ensure this matches your form input name
                processAcceptRequest(taskId, assignedUserId);
                break;

                //refuse request of user
            case "DELETE":
                processDeclineRequest(requestToProcess);
                break;
            case "REJECT":
                 break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
        }

        // Redirect back to the request list
        response.sendRedirect("requestTask?action=list");
    }

    private void processDeclineRequest(Optional<TaskRequest> request) {
        if (!request.isPresent()) {
            throw new IllegalArgumentException("Request is not present.");
        }

        TaskRequest req = request.get();
        Task task = req.getTask();
        User user = req.getUser();

        if (user == null) {
            throw new NullPointerException("User associated with the request is null.");
        }

        if (task == null) {
            throw new NullPointerException("Task associated with the request is null.");
        }

        if (req.getType() == TypeRequest.REJECT) {
            processDailyToken(user);
        } else if (req.getType() == TypeRequest.DELETE) {
            processMonthlyToken(user);
        }

        // Change the request status to REJECTED instead of deleting the request
        taskRequestService.updateRequestStatus(req.getId(), StatusRequest.REJECTED); // Pass ID and status separately
    }

    private void processDailyToken(User user) {
        if (user.getDailyToken() == 0) {
            userService.updateDailyTokens((long) user.getId(), user.getDailyToken() + 1); // Return the used daily token
        }
        userService.updateDailyTokens((long) user.getId(), user.getDailyToken() * 2); // Double the daily tokens
    }

    private void processMonthlyToken(User user) {
        if (user.getMonthlyToken() == 0) {
            userService.updateMonthlyTokens((long) user.getId(), user.getMonthlyToken() + 1); // Return the used monthly token
        }
        userService.updateMonthlyTokens((long) user.getId(), user.getMonthlyToken() * 2); // Double the monthly tokens
    }


    private void processAcceptRequest(int taskId, Long assignedUserId) {
        System.out.println("test test test test ");
        Optional<Task> t = taskService.getTaskById(taskId);

        if (t.isEmpty()) {
            throw new IllegalArgumentException("Task not found.");
        }

        User assignedUser = userService.findUserById(assignedUserId);

        if (assignedUser == null) {
            throw new IllegalArgumentException("Assigned user not found.");
        }

        // Update the task with new assigned user and mark it as refused
        Task task=t.get();
        task.setUser_assigne(assignedUser);
        task.setRefused(true); // Change the status to refused
        taskService.updateTask(task); // Save changes to the database
    }


}
