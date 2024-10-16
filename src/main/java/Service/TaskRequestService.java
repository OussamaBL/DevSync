package Service;

import Exceptions.*;
import Model.Enums.StatusRequest;
import Model.Enums.UserType;
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
    private final UserService userService;
    private final TaskService taskService;

    public TaskRequestService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRequestRepository = new TaskRequestRepository(entityManager);
        this.userService=new UserService();
        this.taskService=new TaskService();
    }


    public TaskRequest saveRequest(TaskRequest taskRequest) {
        if(userService.findUserById(taskRequest.getUser().getId())==null)
            throw new UserNotExistException("user in request not exist");
        if(taskService.getTaskById(taskRequest.getTask().getId())==null)
            throw new TaskExistException("Task in request not exist");
        if(taskRequestRepository.findRequestTask(taskRequest)!=null)
            throw new TaskExistException("task already have request (Refused or aleady assign to another user or still in pending)");
        if(taskRequest.getUser().getDailyToken()<1)
            throw new RequestValidationException("Daily token not found");
        if(taskRequest.getUser().getRole_user()== UserType.MANAGER)
            throw new FormatIncorrectException("User should not be a manager");
        if(taskRequest.getTask().getIsRefused())
            throw new TaskValidationException("the task already rejected");

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
            throw new RequestValidationException("request not found with id: " + taskrequestId);
        }
    }

    public Optional<TaskRequest> getRequestById(int id) {
        if(id<=0) throw new RequestValidationException("id de taskrequest should be superieur than 0");
        return taskRequestRepository.findById(id);
    }

    public List<TaskRequest> getAllRequests() {
        return taskRequestRepository.findAll();
    }
    public List<TaskRequest> getRequestbyUser(Long id) {
        if(userService.findUserById(id)==null)
            throw new UserNotExistException("user not exist");
        return taskRequestRepository.getRequestbyUser(id);
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
            throw new RequestValidationException("request not found with id: " + requestId);
        }
    }
}
