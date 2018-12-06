package DAO;

import entity.User;
import utilities.DBUtility;

import java.util.List;

public class UserDaoImpl implements UserDao{
    private DBUtility DBUtility;

    public UserDaoImpl(String pathAccount) {
        DBUtility = DBUtility.getInstance(pathAccount);
    }

    @Override
    public List<User> getAllUsers() {
        return DBUtility.getAllUser();
    }

    @Override
    public User getUser(int id) {
        return DBUtility.getUserById(id);
    }

    @Override
    public void createOrUpdateUser(User user) {
        DBUtility.createOrUpdateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        DBUtility.deleteUser(id);
    }
}
