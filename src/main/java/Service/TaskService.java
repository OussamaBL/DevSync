package Service;

import Model.Task;
import Model.User;
import Repository.TaskRepository;
import Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRepository = new TaskRepository(entityManager);
    }
    public void insertTask(Task task){
        task.validateDates();
        taskRepository.addTask(task);

    }
    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }
    public List<Task> getTasksUser(User user){
        return taskRepository.getTasksUser(user);
    }
}
