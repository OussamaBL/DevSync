package Dao.Interfaces;

import Model.User;

import java.util.List;

public interface UserInterface {
    User getUserById(int id);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}
