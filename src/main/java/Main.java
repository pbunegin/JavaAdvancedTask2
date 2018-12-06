import service.Service;
import service.ServiceImpl;
import service.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private final static String pathAccount = "src\\main\\java\\accounts";

    public static void main(String[] args) {
        Service service = new ServiceImpl(pathAccount);

        service.getAllUsers().forEach(System.out::println);

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future> futures = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            futures.add(executorService.submit(new Transaction(service)));
        }
        executorService.shutdown();

        for (Future future: futures){
            while (!future.isDone()){
            }
        }

        System.out.println("-------------------------------");
        service.getAllUsers().forEach(System.out::println);
    }
}
