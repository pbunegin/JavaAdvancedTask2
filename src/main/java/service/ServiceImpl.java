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
        return userDao.getUser(id);
    }

    @Override
    public User getUserByName(String name) {
        for (User user : userDao.getAllUsers()) {
            if (user.getName().equals(name))
                return user;
        }
        return null;
    }

    @Override
    public void createUser(int id, String name, long balance) {
        userDao.createOrUpdateUser(new User(id,name,balance));
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public void createUsers(List<User> users) {
        for (User user: users){
            userDao.createOrUpdateUser(user);
        }
    }

    @Override
    public User getRandomUser(int maxId) {
        return getUserById((int) (Math.random()*maxId));
    }
}
