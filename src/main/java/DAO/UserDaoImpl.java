package DAO;

import entity.User;
import utilities.DBUtility;

import java.util.List;

public class UserDaoImpl implements UserDao{
    private DBUtility dbUtility;

    public UserDaoImpl(String pathAccount) {
        this.dbUtility = DBUtility.getInstance(pathAccount);
    }

    @Override
    public List<User> getAllUsers() {
        return dbUtility.getAllUser();
    }

    @Override
    public User getUser(int id) {
        return dbUtility.getUserById(id);
    }

    @Override
    public void createOrUpdateUser(User user) {
        dbUtility.createOrUpdateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        dbUtility.deleteUser(id);
    }
}
