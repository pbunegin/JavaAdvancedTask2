package service;

import DAO.UserDao;
import entity.User;

import java.util.List;

public class ServiceImpl implements Service {
    private UserDao userDao;

    public ServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public void createUser(int id, String name, long balance) {
        userDao.createUser(id,name,balance);
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }
}
