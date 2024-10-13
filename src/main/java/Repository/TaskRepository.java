package Repository;

import Model.Task;
import Model.User;
import Repository.Interfaces.TaskInterface;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements TaskInterface {
    private final EntityManager entityManager;

    public TaskRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        try {
            Task t = entityManager.find(Task.class, id);
            return Optional.of(t);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("error for Task by id: " + e.getMessage());
        }
    }

    @Override
    public List<Task> getAllTasks() {
        try {
            TypedQuery<Task> query = entityManager.createQuery(
                    "SELECT t FROM Task t " +
                            "JOIN FETCH t.user_create " +
                            "JOIN FETCH t.user_assigne " +
                            "LEFT JOIN FETCH t.listTags",
                    Task.class
            );
            List<Task> listTasks=query.getResultList();
            listTasks.forEach(task -> entityManager.refresh(task));
            return listTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Task> getTasksUser(User user) {
        try {
            TypedQuery<Task> query = entityManager.createQuery(
                            "SELECT t FROM Task t WHERE t.user_create.id = :userId OR t.user_assigne.id = :userId",
                            Task.class)
                    .setParameter("userId", user.getId());
            List<Task> listTasks=query.getResultList();
            listTasks.forEach(task -> entityManager.refresh(task));
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

    public void changeStatusTask(Task task){
        EntityTransaction transaction= entityManager.getTransaction();
        try{
            transaction.begin();
            Query query = entityManager.createQuery("UPDATE Task t SET t.status = :status WHERE t.id = :taskId");
            query.setParameter("status", task.getStatus());
            query.setParameter("taskId", task.getId());

            int rowsUpdated = query.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No task found with ID: " + task.getId());
            }

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
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteTask(Long id) {

    }

    @Override
    public void deleteTask(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Task task = entityManager.find(Task.class, id);
            if (task != null) {
                entityManager.remove(task);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
