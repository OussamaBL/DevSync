package Repository;

import Model.Task;
import Model.User;
import Repository.Interfaces.TaskInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements TaskInterface {
    private final EntityManager entityManager;

    public TaskRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }
    @Override
    public Task getTaskById(Long id) {
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        try {
            List<Task> listTasks= entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
            return listTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<Task> getTasksUser(User user) {
        try {
            List<Task> listTasks = entityManager.createQuery(
                            "SELECT t FROM Task t WHERE t.user_create.id = :userId OR t.user_assigne.id = :userId",
                            Task.class)
                    .setParameter("userId", user.getId())
                    .getResultList();
            return listTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void addTask(Task task) {
        EntityTransaction transaction= entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(task);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void deleteTask(Long id) {

    }
}
