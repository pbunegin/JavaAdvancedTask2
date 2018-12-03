package DAO;

import entity.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByName(String name);
    void createUser(int id, String name, long balance);
    void deleteUser(int id);
}
