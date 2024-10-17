package Controller;

import Model.Enums.TaskStatus;
import Model.Tag;
import Model.Task;
import Model.User;
import Service.TagService;
import Service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "stats",urlPatterns = {"/stats"})
public class StatServlet extends HttpServlet {
    private TaskService taskService = new TaskService();
    private TagService tagService = new TagService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        List<Task> allTasks = taskService.getAllTasks();
        List<Tag> allTags = tagService.getAllTags();
        request.setAttribute("allTags", allTags);
        String selectedTagsParam = request.getParameter("selected_tags");
        List<Task> filteredTasks;

        if (selectedTagsParam != null && !selectedTagsParam.isEmpty()) {
            String[] selectedTagIds = selectedTagsParam.split(",");

            filteredTasks = allTasks.stream()
                    .filter(task -> task.getListTags().stream()
                            .anyMatch(tag -> Arrays.asList(selectedTagIds).contains(String.valueOf(tag.getId())))
                    )
                    .collect(Collectors.toList());
        } else {
            filteredTasks = allTasks;
        }

        String startDateParam = request.getParameter("start_date");
        String endDateParam = request.getParameter("end_date");

        if ((startDateParam != null && !startDateParam.isEmpty()) || (endDateParam != null && !endDateParam.isEmpty())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate startDate = LocalDate.parse(startDateParam, formatter);
            LocalDate endDate = LocalDate.parse(endDateParam, formatter);

            filteredTasks = filteredTasks.stream()
                    .filter(task -> {
                        LocalDate taskDate = task.getDate_create();
                        boolean isAfterStart = !taskDate.isBefore(startDate);
                        boolean isBeforeEnd = !taskDate.isAfter(endDate);
                        return isAfterStart && isBeforeEnd;
                    })
                    .collect(Collectors.toList());

        } else {
            filteredTasks = allTasks;
        }

        request.setAttribute("tasks", filteredTasks);

        long totalTasks = filteredTasks.size();

        if (totalTasks > 0) {
            long tasksEn_cours = filteredTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
            long tasksEn_attend = filteredTasks.stream().filter(t -> t.getStatus() == TaskStatus.NOT_STARTED).count();
            long tasksTermine = filteredTasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
            long tasksInComplete = filteredTasks.stream().filter(t -> t.getStatus() == TaskStatus.OVERDUE).count();

            double percentageEn_cours = (double) tasksEn_cours / totalTasks * 100;
            double percentageEn_attend = (double) tasksEn_attend / totalTasks * 100;
            double percentageTermine = (double) tasksTermine / totalTasks * 100;
            double percentageInComplete = (double) tasksInComplete / totalTasks * 100;

            request.setAttribute("percentageEn_cours", percentageEn_cours);
            request.setAttribute("percentageEn_attend", percentageEn_attend);
            request.setAttribute("percentageTermine", percentageTermine);
            request.setAttribute("percentageInComplete", percentageInComplete);
        } else {
            request.setAttribute("percentageEn_cours", 0.0);
            request.setAttribute("percentageEn_attend", 0.0);
            request.setAttribute("percentageTermine", 0.0);
            request.setAttribute("percentageInComplete", 0.0);
        }

        request.getRequestDispatcher("Stat.jsp").forward(request, response);
    }

}
