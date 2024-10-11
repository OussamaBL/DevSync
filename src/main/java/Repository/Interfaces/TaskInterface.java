package Repository.Interfaces;

import Model.Task;
import Model.User;

import java.util.List;

public interface TaskInterface {
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    void addTask(Task task);
    void updateTask(Task task);
    void deleteTask(Long id);

    void deleteTask(int id);
}
