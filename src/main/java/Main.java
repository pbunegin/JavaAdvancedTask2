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

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0; i < 1000; i++) {
            executorService.submit(new Transaction(service.getRandomUser(),service.getRandomUser(), (long) (Math.random()*2000+1)));
        }
        executorService.shutdown();
    }
}
