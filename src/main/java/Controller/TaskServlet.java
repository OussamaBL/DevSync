package Controller;

import Model.Enums.TaskStatus;
import Model.Enums.UserType;
import Model.Tag;
import Model.Task;
import Model.User;
import Service.TaskService;

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

@WebServlet(name = "tasks",urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {

    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        try {
            taskService = new TaskService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TaskServlet", e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            User user=(User) request.getSession().getAttribute("user");
            List<Task> listTasks=new ArrayList<>();
            if(user.getRole_user().name().equals("MANAGER")){
                listTasks=taskService.getAllTasks();
            }
            else{
                listTasks=taskService.getTasksUser(user);
            }
            request.setAttribute("listTasks",listTasks);
            RequestDispatcher requestDispatcher=request.getRequestDispatcher("listTasks.jsp");
            requestDispatcher.forward(request,response);

         } else if (action.equals("edit")) {
            /*Long id = Long.parseLong(request.getParameter("id"));
            User user = userService.findUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("FormUser.jsp").forward(request, response);*/
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalDate dateCreate = LocalDate.parse(request.getParameter("date_create"));
            LocalDate dateFin = request.getParameter("date_fin") != null ? LocalDate.parse(request.getParameter("date_fin")) : null;
            String statusStr = request.getParameter("status");
            TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());

            String[] tagNames = request.getParameter("tags").split(",");
            List<Tag> tags = new ArrayList<>();
            for (String tagName : tagNames) {
                Tag tag = new Tag(tagName.trim());
                tags.add(tag);
            }

            Task task = new Task(title,description,dateCreate,dateFin,status);
            task.setListTags(tags);

            taskService.insertTask(task);
            response.sendRedirect("tasks?status=success");
        } catch (Exception e) {
            response.sendRedirect("tasks?status="+e.getMessage());
            //e.printStackTrace();
        }
    }



    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("users");*/
    }

}
