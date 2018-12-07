package service;

import DAO.UserDao;
import DAO.UserDaoImpl;
import entity.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ServiceImpl implements Service {
    private static final Random RANDOM = new Random();
    private static final String PATH_ALL_NAMES = "src\\main\\java\\service\\names.txt";
    private static final int MAX_BALANCE = 10000;
    private UserDao userDao;
    private List<User> users;
    private AtomicInteger countTransaction = new AtomicInteger(0);

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
            if (user != null){
                users.add(user);
            }
        }
        return user;
    }

    @Override
    public User getUserByName(String name) {
        return users.stream().filter(e->e.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public void createOrUpdateUser(int id, String name, long balance) {
        User user = new User(id, name, balance);
        userDao.createOrUpdateUser(user);
        //обновить users
        getUserById(id);
    }

    @Override
    public void deleteUser(int id) {
        users = users.stream().filter(e->e.getId()!=id).collect(Collectors.toList());
        userDao.deleteUser(id);
    }

    @Override
    public void createUsers(List<User> users) {
        for (User user: users){
            userDao.createOrUpdateUser(user);
        }
    }

    @Override
    public int getCountTransaction() {
        return countTransaction.getAndIncrement();
    }

    @Override
    public void decrementCountTransaction(){
        countTransaction.decrementAndGet();
    }

    @Override
    public List<User> createRandomUsers(int maxNumberUsers) throws IOException {
        List<String> stringNames = Files.readAllLines(Paths.get(PATH_ALL_NAMES));
        Collections.shuffle(stringNames);
        users.clear();
        for (int i = 0; i < maxNumberUsers; i++) {
            users.add(new User(i,stringNames.get(i),RANDOM.nextInt(MAX_BALANCE)));
        }
        createUsers(users);
        return users;
    }

    @Override
    public User getRandomUser() {
        int num = RANDOM.nextInt(users.size()+2);
        return getUserById(num);
    }
}
