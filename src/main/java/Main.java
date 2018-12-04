import DAO.UserDao;
import DAO.UserDaoImpl;
import entity.User;
import service.Service;
import service.ServiceImpl;
import service.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static String pathAccount = "src\\main\\java\\accounts\\";

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl(pathAccount);
        Service service = new ServiceImpl(userDao);

        List<User> allUsers = service.getAllUsers();
        for (User user: allUsers) {
            System.out.println(user);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for(int i = 0; i < 1000; i++) {
            long sum = (long) (Math.random() * 500 + 1);
            Transaction task = new Transaction(service.getRandomUser(30), service.getRandomUser(30), sum);
            executorService.submit(task);
        }
        executorService.shutdown();
    }
}
