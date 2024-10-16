package Controller;

import Model.Enums.TaskStatus;
import Model.Enums.UserType;
import Model.Tag;
import Model.Task;
import Model.User;
import Service.TagService;
import Service.TaskService;
import Service.UserService;

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
import java.util.Optional;

@WebServlet(name = "tasks",urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private UserService userService;
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        try {
            taskService = new TaskService();
            userService = new UserService();
            tagService = new TagService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TaskServlet", e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           String action = request.getParameter("action");

           if (action.equals("list")) {
               User user = (User) request.getSession().getAttribute("user");
               List<Task> listTasks = new ArrayList<>();
               if (user.getRole_user() == UserType.MANAGER)
                   listTasks = taskService.getAllTasks();
               else listTasks = taskService.getTasksUser(user);

               request.setAttribute("listTasks", listTasks);
               RequestDispatcher requestDispatcher = request.getRequestDispatcher("listTasks.jsp");
               requestDispatcher.forward(request, response);

           }  else if (action.equals("newTask")) {
               List<User> userList = userService.findAllUsers();
               request.setAttribute("userList", userList);
               RequestDispatcher requestDispatcher = request.getRequestDispatcher("newTask.jsp");
               requestDispatcher.forward(request, response);
           }
       }
       catch (Exception e) {
           response.sendRedirect("tasks?action=list&status=" + e.getMessage());
       }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String method = request.getParameter("_method");
            if ("addTask".equalsIgnoreCase(method)) {
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                LocalDate date_start = LocalDate.parse(request.getParameter("date_start"));
                LocalDate date_fin = LocalDate.parse(request.getParameter("date_fin"));
                String[] tagNames = request.getParameter("tags").split(",");
                List<Tag> tags = new ArrayList<>();
                Tag tag;
                for (String tagName : tagNames) {
                    Optional<Tag> t = tagService.findByName(tagName.trim());
                    tag=new Tag();
                    if (!t.isPresent()) {
                        tag.setName(tagName.trim());
                        tag= tagService.insertTag(tag);
                    }
                    else{
                        tag.setId(t.get().getId());
                        tag.setName(t.get().getName());
                    }
                    tags.add(tag);
                }

                User user_auth = (User) request.getSession().getAttribute("user");
                long user_assigne_id;

                if (user_auth.getRole_user()== UserType.MANAGER)
                    user_assigne_id = Long.parseLong(request.getParameter("user_assigne_id"));
                else
                    user_assigne_id = user_auth.getId();

                Task task = new Task(title, description, date_start, date_fin, new User(user_auth.getId()), new User(user_assigne_id), TaskStatus.NOT_STARTED);
                task.setListTags(tags);
                taskService.insertTask(task,user_auth);

                //change status task
            } else if ("changeStatus".equalsIgnoreCase(method)) {
                int id = Integer.parseInt(request.getParameter("id_task"));
                LocalDate date_fin = LocalDate.parse(request.getParameter("date_fin"));
                TaskStatus status = TaskStatus.valueOf(request.getParameter("status"));
                taskService.changeStatusTask(new Task(id,date_fin,status));
                //delete task (without jeton)
            } else if ("deleteTask".equalsIgnoreCase(method)) {
                int id = Integer.parseInt(request.getParameter("id"));
                User user_auth = (User) request.getSession().getAttribute("user");
                taskService.deleteTask(id,user_auth);
            }
            //delete with jeton
            else if ("delete_token".equalsIgnoreCase(method)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                User user_auth = (User) request.getSession().getAttribute("user");
                taskService.deleteTaskToken(taskId,user_auth);

            }
            response.sendRedirect("tasks?action=list&status=success");
        }
        catch (Exception e) {
            response.sendRedirect("tasks?action=list&status=" + e.getMessage());
        }
    }

}
