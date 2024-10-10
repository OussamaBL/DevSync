package Service;

import Model.Task;
import Model.User;
import Repository.TaskRepository;
import Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRepository = new TaskRepository(entityManager);
    }
    public void insertTask(Task task,User user_auth){
        if (task == null || task.equals(new Task())) {
            throw new RuntimeException("task information cannot be null or empty.");
        }

        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new RuntimeException("task title cannot be null or empty.");
        }
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new RuntimeException("task description cannot be null or empty.");
        }
        if (task.getDate_start() == null) {
            throw new RuntimeException("start date cannot be null.");
        }
        if (!task.getDate_fin().isAfter(task.getDate_start())) {
            throw new RuntimeException("start due should be after or equal start date.");
        }
        if (task.getUser_assigne() == null) {
            throw new RuntimeException("assigned user cannot be null.");
        }

        if (user_auth.getId() != task.getUser_assigne().getId()) {
            if (!task.getDate_start().isAfter(LocalDate.now().plusDays(3))) {
                throw new RuntimeException("start date should be at least 3 days after the current date.");
            }
        }

        taskRepository.addTask(task);

    }
    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }
    public List<Task> getTasksUser(User user){
        return taskRepository.getTasksUser(user);
    }
}
