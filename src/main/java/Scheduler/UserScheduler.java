package Scheduler;

import Model.User;
import Service.UserService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final UserService userService = new UserService();

    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateUsers, 0, 24, TimeUnit.HOURS);
    }

    private void checkAndUpdateUsers() {
        try {
            List<User> users = userService.findAllUsers();
            for (User user : users) {
                if (user.getDailyToken() != 2 ) {
                    user.setDailyToken(2);
                    userService.updateUser(user);
                }
                if(user.getMonthlyToken()!=1){
                    user.setMonthlyToken(1);
                    userService.updateUser(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des users : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
