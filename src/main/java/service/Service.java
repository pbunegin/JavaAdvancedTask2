package service;

import entity.User;

import java.io.IOException;
import java.util.List;

public interface Service {
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByName(String name);
    void createOrUpdateUser(int id, String name, long balance);
    void deleteUser(int id);
    void createUsers(List<User> users);
    User getRandomUser();
    int getCountTransaction();
    void decrementCountTransaction();
    List<User> createRandomUsers(int i) throws IOException;
}
