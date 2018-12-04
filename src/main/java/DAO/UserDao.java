package DAO;

import entity.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUser(int id);
    void createOrUpdateUser(User user);
    void deleteUser(int id);
}
