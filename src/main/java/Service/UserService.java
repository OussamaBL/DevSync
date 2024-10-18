package Service;

import Exceptions.FormatIncorrectException;
import Exceptions.UserNotExistException;
import Model.Task;
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
        if (user == null || user.equals(new User())) {
            throw new UserNotExistException("User information cannot be null or empty.");
        }
        if (findByUserName(user.getUsername())!=null) {
            throw new UserNotExistException("UserName already exist");
        }
        if (findByEmail(user.getEmail())!=null) {
            throw new UserNotExistException("Email already exist");
        }
        userRepository.addUser(user);
    }

    public void updateUser(User user) {
        if (user == null || user.equals(new User())) {
            throw new UserNotExistException("User information cannot be null or empty.");
        }
        if (findUserById(user.getId())==null) {
            throw new UserNotExistException("User not exist");
        }
        userRepository.updateUser(user);
    }

    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        if (user != null) {
            userRepository.deleteUser(user);
        }
        else throw new UserNotExistException("User with id"+userId+" not exist");

    }

    public User findUserById(Long userId) {
        if(userId<=0) throw new FormatIncorrectException("userId should be superieur than 0");
        return userRepository.getUserById(userId);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public List<User> findAllUsers() {
        return userRepository.getAllUsers();
    }
    public List<User> getUsersStatus(){
        return userRepository.getUsersStatus();
    }

    public void updateUserTokens(Long userId, Integer dailyTokens, Integer monthlyTokens) {
        User user = userRepository.getUserById(userId);
        if(user!=null) {
            if (dailyTokens != null) {
                user.setDailyToken(dailyTokens);
            }
            else throw new FormatIncorrectException("Format of "+dailyTokens+" incorrect");
            if (monthlyTokens != null) {
                user.setMonthlyToken(monthlyTokens);
            }
            else throw new FormatIncorrectException("Format of "+monthlyTokens+" incorrect");
            userRepository.updateUser(user);
        }
        else throw new UserNotExistException("user with id "+userId+" not exists");
    }

    public void updateDailyTokens(Long userId, Integer dailyTokens) {
        User user = userRepository.getUserById(userId);
        if(user!=null) {
            if (dailyTokens != null) {
                user.setDailyToken(dailyTokens);
                userRepository.updateUser(user);
            }
            else throw new FormatIncorrectException("Format of "+dailyTokens+" incorrect");
        }
        else throw new UserNotExistException("user not exists");
    }

    // New method to update only monthly tokens
    public void updateMonthlyTokens(Long userId, Integer monthlyTokens) {

        User user = userRepository.getUserById(userId);
        if(user!=null){
            if (monthlyTokens != null) {
                user.setMonthlyToken(monthlyTokens);
                userRepository.updateUser(user);
            }
            else throw new FormatIncorrectException("Format of "+monthlyTokens+" incorrect");
        }
        else throw new UserNotExistException("user not exists");

    }
}
