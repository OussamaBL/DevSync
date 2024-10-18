package Repository;

import Model.Enums.StatusRequest;
import Model.Task;
import Model.TaskRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class TaskRequestRepository {
    private EntityManager entityManager;

    public TaskRequestRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public TaskRequest save(TaskRequest taskRequest) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(taskRequest);
            transaction.commit();
            return taskRequest;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    public TaskRequest updatetaskrequest(TaskRequest taskRequest) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(taskRequest);
            transaction.commit();
            return taskRequest;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }



    public Optional<TaskRequest> findById(int id) {

        TypedQuery<TaskRequest> query = entityManager.createQuery(
                        "SELECT t FROM TaskRequest t join fetch t.task join fetch t.user WHERE t.id = :id",
                        TaskRequest.class)
                .setParameter("id", id);

        return Optional.of(query.getSingleResult());
    }

    public void delete(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            TaskRequest request = entityManager.find(TaskRequest.class, id);
            if (request != null) {
                entityManager.remove(request);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("error in deleting the request: " + e.getMessage(), e);
        }
    }

    public List<TaskRequest> findAll() {
        TypedQuery<TaskRequest> query = entityManager.createQuery("SELECT t FROM TaskRequest t join fetch t.user join fetch t.task", TaskRequest.class);
        List<TaskRequest> taskRequests=query.getResultList();
        taskRequests.forEach(taskRequest -> entityManager.refresh(taskRequest));
        return taskRequests;
    }
    public List<TaskRequest> getRequestbyUser(Long id) {
        TypedQuery<TaskRequest> query = entityManager.createQuery(
                        "SELECT t FROM TaskRequest t join fetch t.user join fetch t.task WHERE t.user.id = :userId",
                        TaskRequest.class)
                .setParameter("userId", id);
        List<TaskRequest> taskRequests=query.getResultList();
        taskRequests.forEach(taskRequest -> entityManager.refresh(taskRequest));
        return taskRequests;
    }
    public TaskRequest findRequestTask(TaskRequest taskRequest){
        try{
            TypedQuery<TaskRequest> query = entityManager.createQuery(
                            "SELECT t FROM TaskRequest t join fetch t.user join fetch t.task WHERE t.task.id = :taskId",
                            TaskRequest.class)
                    .setParameter("taskId", taskRequest.getTask().getId());
            TaskRequest req=query.getSingleResult();
            return req;
        }
        catch (NoResultException e) {
            return null;
        }

    }


    public void updateRequestStatus(int taskrequestId, StatusRequest status) {
        entityManager.getTransaction().begin();
        TaskRequest taskRequest = entityManager.find(TaskRequest.class, taskrequestId);
        if (taskRequest != null) {
            taskRequest.setStatus(status);
            entityManager.merge(taskRequest);
        }
        entityManager.getTransaction().commit();
    }
}
