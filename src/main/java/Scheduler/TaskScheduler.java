package Scheduler;

import Model.Enums.TaskStatus;
import Model.Task;
import Service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TaskService taskService = new TaskService();

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateTasks, 0, 1, TimeUnit.MINUTES);
    }

    private void checkAndUpdateTasks() {
        try {
            List<Task> tasks = taskService.getAllTasks();
            for (Task task : tasks) {
                if (task.getDate_fin().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.COMPLETED) {
                    task.setStatus(TaskStatus.OVERDUE);
                    taskService.changeStatusTask(task);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des tâches : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
