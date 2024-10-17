package Service;

import Exceptions.FormatIncorrectException;
import Exceptions.TaskExistException;
import Exceptions.TaskValidationException;
import Exceptions.UserNotExistException;
import Model.Enums.TaskStatus;
import Model.Enums.UserType;
import Model.Task;
import Model.TaskRequest;
import Model.User;
import Repository.TaskRepository;
import Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRepository = new TaskRepository(entityManager);
        this.userService = new UserService();
    }
    public void insertTask(Task task,User user_auth){
        if (task == null || task.equals(new Task())) {
            throw new TaskValidationException("task information cannot be null or empty.");
        }
        if (!task.getDate_fin().isAfter(task.getDate_start())) {
            throw new TaskValidationException("start due should be after or equal start date.");
        }
        if (userService.findUserById(task.getUser_assigne().getId())==null){
            throw new UserNotExistException("assigned user not exists");
        }
        if (userService.findUserById(task.getUser_create().getId())==null){
            throw new UserNotExistException("created user not exists");
        }
        if (task.getListTags().isEmpty()) {
            throw new TaskValidationException("Tags are required.");
        }

        if (user_auth.getId() != task.getUser_assigne().getId()) {
            if (!task.getDate_start().isAfter(LocalDate.now().plusDays(3))) {
                throw new TaskValidationException("start date should be at least 3 days after the current date.");
            }
        }

        taskRepository.addTask(task);

    }
    public void changeStatusTask(Task task){
        if(task.getStatus()==TaskStatus.COMPLETED && !task.getDate_fin().isAfter(LocalDate.now()))
            throw new TaskValidationException("marking a task as completed must be done before the due date.");
        taskRepository.changeStatusTask(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }

    public List<Task> getTasksUser(User user){
        if (userService.findUserById(user.getId())==null) {
            throw new UserNotExistException("user not exist");
        }
        return taskRepository.getTasksUser(user);
    }

    public Optional<Task> getTaskById(int id){
        if(id<=0) throw new FormatIncorrectException("id should be superieur than 0");
        return taskRepository.getTaskById(id);
    }
    public void deleteTask(int id,User user_auth){
        if(id>0){
            Optional<Task> task=this.getTaskById(id);
            if(task.isPresent()){
                Task t=task.get();
                taskRepository.deleteTask(id);
            }
            else throw new TaskExistException("Task not exists");
        }
        else throw new FormatIncorrectException("format incorrect");

    }
    public void deleteTaskToken(int id,User user_auth){
        Optional<Task> task=this.getTaskById(id);
        if(task.isPresent()){
            Task t=task.get();

            if(user_auth.getRole_user() == UserType.USER){
                if(user_auth.getId() != t.getUser_create().getId()){
                    if(user_auth.getMonthlyToken()>0){
                        //if(!t.getIsRefused()){
                            taskRepository.deleteTask(id);
                            userService.updateMonthlyTokens(user_auth.getId(),user_auth.getMonthlyToken()-1);
                        /*}
                        else throw new TaskValidationException("the task already refused");*/

                    }
                    else throw new TaskValidationException("no Monthly token for the user auth");
                }
                else throw new TaskValidationException("to delete with token the task should be created by user different from the user auth");
            }
            else throw new TaskValidationException("user role shouls be (USER)");

            if( user_auth.getMonthlyToken()<1){
                throw new TaskValidationException("the user don't have the jetons to delete the task");
            }



        }
        else throw new TaskExistException("Task not exists");
    }

    public void updateTask(Task task){
        if (task == null || task.equals(new Task())) {
            throw new TaskValidationException("task information cannot be null or empty.");
        }
        if (!task.getDate_fin().isAfter(task.getDate_start())) {
            throw new TaskValidationException("start due should be after or equal start date.");
        }
        if (userService.findUserById(task.getUser_assigne().getId())==null){
            throw new UserNotExistException("assigned user not exists");
        }
        if (userService.findUserById(task.getUser_create().getId())==null){
            throw new UserNotExistException("created user not exists");
        }
         taskRepository.updateTask(task);
    }


    public List<Task> getTasksByPeriod(Long idUser, String period, String tagFilter) {
        if (userService.findUserById(idUser)==null)
            throw new UserNotExistException("user not exists");
        if (period.equals("week") || period.equals("month") || period.equals("year")){
            return taskRepository.getTasksByPeriod(idUser,period,tagFilter);
        }
        else
            throw new FormatIncorrectException("Format of perdiod incorrect");


    }

    public double calculateCompletionPercentage(List<Task> tasks) {
        long completedTasks = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();
        return (double) completedTasks / tasks.size() * 100;
    }


}
