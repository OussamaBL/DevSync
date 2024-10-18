package Repository;

import Model.Task;
import Model.User;
import Repository.Interfaces.TaskInterface;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
                entityManager.refresh(task.getUser_create());
                entityManager.refresh(task.getUser_assigne());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByPeriod(Long idUser, String period, String tagFilter) {
        try{
            StringBuilder queryStr = new StringBuilder("SELECT t FROM Task t JOIN t.user_create ua JOIN t.listTags tag WHERE ua.id = :idUser");

            if (tagFilter != null && !tagFilter.isEmpty()) {
                queryStr.append(" AND tag.name = :tagFilter");
            }

            switch (period.toLowerCase()) {
                case "week":
                    queryStr.append(" AND t.date_create BETWEEN :startWeek AND :endWeek");
                    break;
                case "month":
                    queryStr.append(" AND t.date_create BETWEEN :startMonth AND :endMonth");
                    break;
                case "year":
                    queryStr.append(" AND t.date_create BETWEEN :startYear AND :endYear");
                    break;
            }

            TypedQuery<Task> query = entityManager.createQuery(queryStr.toString(), Task.class);
            query.setParameter("idUser", idUser);

            if (tagFilter != null && !tagFilter.isEmpty()) {
                query.setParameter("tagFilter", tagFilter);
            }

            if ("week".equalsIgnoreCase(period)) {
                query.setParameter("startWeek", LocalDate.now().with(DayOfWeek.MONDAY));
                query.setParameter("endWeek", LocalDate.now().with(DayOfWeek.SUNDAY));
            } else if ("month".equalsIgnoreCase(period)) {
                query.setParameter("startMonth", LocalDate.now().withDayOfMonth(1));
                query.setParameter("endMonth", LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
            } else if ("year".equalsIgnoreCase(period)) {
                query.setParameter("startYear", LocalDate.now().withDayOfYear(1));
                query.setParameter("endYear", LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()));
            }

            return query.getResultList();
        }
        catch (Exception e) {
            return null;
        }

    }

}
