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
    private final static String pathAccount = "src\\main\\java\\accounts";

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl(pathAccount);
        Service service = new ServiceImpl(userDao);

        List<User> allUsers = service.getAllUsers();
        for (User user: allUsers) {
            System.out.println(user);
        }
        service.getUserById(40);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0; i < 1000; i++) {
            long sum = (long) (Math.random() * 500 + 1);
            int num1 = (int) (Math.random()*30);
            int num2 = (int) (Math.random()*30);
            Transaction task = new Transaction(allUsers.get(num1), allUsers.get(num2), sum);
            executorService.submit(task);
        }
        executorService.shutdown();
    }
}
