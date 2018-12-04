package DAO;

import entity.User;
import utilities.DaoUtility;

import java.util.List;

public class UserDaoImpl implements UserDao{
    private DaoUtility daoUtility;

    public UserDaoImpl(String pathAccount) {
        daoUtility = DaoUtility.getInstance(pathAccount);
    }

    @Override
    public List<User> getAllUsers() {
        return daoUtility.getAllUser();
    }

    @Override
    public User getUser(int id) {
        return daoUtility.getUserById(id);
    }

    @Override
    public void createOrUpdateUser(User user) {
        daoUtility.createOrUpdateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        daoUtility.deleteUser(id);
    }
}
