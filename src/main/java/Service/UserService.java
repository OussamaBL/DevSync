package Service;

import Model.User;
import Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.userRepository = new UserRepository(entityManager);
    }

    public void insertUser(User user) {
        userRepository.addUser(user);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.getUserById(userId);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.getAllUsers();
    }
    public List<User> getUsersStatus(){
        return userRepository.getUsersStatus();
    }

    public void updateUserTokens(Long userId, Integer dailyTokens, Integer monthlyTokens) {
        User user = userRepository.getUserById(userId);
        if (dailyTokens != null) {
            user.setDailyToken(dailyTokens);
        }
        if (monthlyTokens != null) {
            user.setMonthlyToken(monthlyTokens);
        }
        userRepository.updateUser(user);
    }

    public void updateDailyTokens(Long userId, Integer dailyTokens) {
        User user = userRepository.getUserById(userId);
        if (dailyTokens != null) {
            user.setDailyToken(dailyTokens);
            userRepository.updateUser(user);
        }
    }

    // New method to update only monthly tokens
    public void updateMonthlyTokens(Long userId, Integer monthlyTokens) {
        User user = userRepository.getUserById(userId);
        if (monthlyTokens != null) {
            user.setMonthlyToken(monthlyTokens);
            userRepository.updateUser(user);
        }
    }
}
