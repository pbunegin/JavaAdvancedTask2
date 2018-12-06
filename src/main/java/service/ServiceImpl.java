package service;

import DAO.UserDao;
import DAO.UserDaoImpl;
import entity.User;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceImpl implements Service {
    public static final int MAX_TRANSACTION = 1000;
    private static final Random RANDOM = new Random();
    private UserDao userDao;
    private List<User> users;
    private static AtomicInteger countTransaction = new AtomicInteger(0);

    public ServiceImpl(String pathAccount) {
        this.userDao = new UserDaoImpl(pathAccount);
    }

    @Override
    public List<User> getAllUsers() {
        if (users==null){
            users = userDao.getAllUsers();
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        User user = users.stream().filter(e->e.getId()==id).findAny().orElse(null);
        if (user == null) {
            user = userDao.getUser(id);
        }
        return user;
    }

    @Override
    public User getUserByName(String name) {
        return users.stream().filter(e->e.getName().equals(name)).findAny().orElse(null);
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

    public int getCountTransaction() {
        return countTransaction.getAndIncrement();
    }

    public void decrementCountTransaction(){
        countTransaction.decrementAndGet();
    }

    @Override
    public User getRandomUser(int maxId) {
        int num = RANDOM.nextInt(maxId);
        return getUserById(num);
    }
}
