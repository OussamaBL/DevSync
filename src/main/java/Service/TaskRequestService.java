package Service;

import Model.Enums.StatusRequest;
import Model.TaskRequest;
import Repository.TaskRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskRequestService {
    private final TaskRequestRepository taskRequestRepository;

    public TaskRequestService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRequestRepository = new TaskRequestRepository(entityManager);
    }


    public TaskRequest saveRequest(TaskRequest taskRequest) {
        taskRequest.setDate_request(LocalDate.now());
        taskRequest.setStatus(StatusRequest.PENDING);
        return taskRequestRepository.save(taskRequest);
    }

    public TaskRequest rejectRequest(int taskrequestId) {
        Optional<TaskRequest> optionalRequest = taskRequestRepository.findById(taskrequestId);
        if (optionalRequest.isPresent()) {
            TaskRequest request = optionalRequest.get();
            request.setStatus(StatusRequest.REJECTED);
            return taskRequestRepository.save(request);
        } else {
            throw new RuntimeException("request not found with id: " + taskrequestId);
        }
    }

    public Optional<TaskRequest> getRequestById(int id) {
        return taskRequestRepository.findById(id);
    }

    public List<TaskRequest> getAllRequests() {
        return taskRequestRepository.findAll();
    }


    public void deleteRequest(int tagId) {
        taskRequestRepository.delete(tagId);
    }


    public TaskRequest updateRequestStatus(int requestId, StatusRequest newStatus) {
        Optional<TaskRequest> optionalRequest = taskRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            TaskRequest request = optionalRequest.get();
            request.setStatus(newStatus);
            return taskRequestRepository.updatetaskrequest(request);
        } else {
            throw new RuntimeException("request not found with id: " + requestId);
        }
    }
}
