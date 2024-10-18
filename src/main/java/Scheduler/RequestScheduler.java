package Scheduler;

import Model.Enums.StatusRequest;
import Model.TaskRequest;
import Model.User;
import Service.TaskRequestService;
import Service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TaskRequestService requestService = new TaskRequestService();
    private final UserService userService = new UserService();


    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateRequest, 0, 1, TimeUnit.MINUTES);
    }
    public boolean isRequestOlderThanHours(TaskRequest request,int hours){
        LocalDateTime created_at = request.getDate_request().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(created_at, now);
        return hoursDifference > hours;

    }

    private void checkAndUpdateRequest() {
        try {
            List<TaskRequest> requests = requestService.getAllRequests();
            for (TaskRequest request : requests) {
                if (isRequestOlderThanHours(request,12) && request.getStatus() == StatusRequest.PENDING) {
                    requestService.updateRequestStatus(request.getId(),StatusRequest.EXPIRE);
                }
                if (isRequestOlderThanHours(request,36) && request.getStatus() == StatusRequest.EXPIRE) {
                    User user = request.getUser();
                    user.setDailyToken(user.getDailyToken() * 2);
                    userService.updateUser(user);

                    System.out.println("Jeton de l'utilisateur mis à jour avec succès.");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vÃ©rification des requests : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
