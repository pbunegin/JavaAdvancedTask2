import entity.User;
import service.Service;
import service.ServiceImpl;
import service.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private final static String pathAccount = "src\\main\\java\\accounts";

    public static void main(String[] args) {
        Service service = new ServiceImpl(pathAccount);

        for (User user: service.getAllUsers()) {
            System.out.println(user);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        Future<?>[] future = null;
        for(int i = 0; i < 40; i++) {
            /*future[i] = */executorService.submit(new Transaction(service));
        }
        executorService.shutdown();

//        for (int i = 0; i < future.length; i++) {
//            while (!future[i].isDone()) {
//            }
//        }
//
//        System.out.println("-------------------------------");
//        for (User user: service.getAllUsers()) {
//            System.out.println(user);
//        }
    }
}
