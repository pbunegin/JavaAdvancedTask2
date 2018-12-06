import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;
import service.ServiceImpl;
import service.Transaction;

import java.io.IOException;
import java.util.concurrent.*;

public class Main {
    private static final String PATH_ACCOUNT = "src\\main\\java\\accounts";
    private static final Service service = new ServiceImpl(PATH_ACCOUNT);
    private static final int MAX_NUMBER_USERS = 30;
    private static final int MAX_RUNNING_THREAD = 20;
    private static final int MAX_NUMBER_TRANSACTIONS = 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Main main = new Main();
        main.clearAndCreateAccounts();
        LOGGER.info("Общая сумма до транзакций: {}\n--------", main.totalBalance());
        main.startTasks();
        LOGGER.info("Общая сумма после транзакций: {}\n--------", main.totalBalance());
        main.printUsers();
    }

    private long totalBalance() {
        return service.getAllUsers().parallelStream().mapToLong(User::getBalance).sum();
    }

    private void printUsers() {
        service.getAllUsers().forEach(user -> LOGGER.info("Пользователь: {}", user));
    }

    private void clearAndCreateAccounts() {
        service.getAllUsers().forEach(user -> service.deleteUser(user.getId()));
        try {
            service.createRandomUsers(MAX_NUMBER_USERS);
        } catch (IOException e) {
            LOGGER.error("Ошибка I/O при попытке создать пользователей: {}", e.getMessage());
        }
        service.getAllUsers().forEach(user -> LOGGER.info("Создан пользователь: {}", user));
    }

    private void startTasks() {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_RUNNING_THREAD);
        for (int i = 0; i < MAX_RUNNING_THREAD; i++) {
            executorService.submit(new Transaction(service, MAX_NUMBER_TRANSACTIONS));
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Ошибка выполнения трэда: ", e.getMessage());
        }
    }
}
