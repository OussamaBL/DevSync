package Repository.Interfaces;

import Model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskInterface {
    Optional<Task> getTaskById(int id);

    List<Task> getAllTasks();
    void addTask(Task task);
    void updateTask(Task task);
    void deleteTask(Long id);

    void deleteTask(int id);
}
