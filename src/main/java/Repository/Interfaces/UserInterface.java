package Repository.Interfaces;

import Model.User;

import java.util.List;

public interface UserInterface {
    User getUserById(Long id);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
}
