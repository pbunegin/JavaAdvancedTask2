package service;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

public class Transaction implements Runnable {
    private Service service;
    private User transferFromUser;
    private User transferToUser;
    private long sum;
    private Logger logger = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        while (service.getCountTransaction() < ServiceImpl.MAX_TRANSACTION) {
            getUsersAndSum();
            if (!check()) {
                service.decrementCountTransaction();
                continue;
            }
            Lock lock1 = transferFromUser.getLock();
            Lock lock2 = transferToUser.getLock();
            if (transferFromUser.getId() < transferToUser.getId()) {
                Lock tempLock = lock1;
                lock1 = lock2;
                lock2 = tempLock;
            }

            lock1.lock();
            lock2.lock();
            try {
                transfer();
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    private boolean check() {
        if (transferFromUser == null || transferToUser == null){
//            logger.warn("Пользователь не существует");
            return false;
        }
        if (transferFromUser.getBalance() - sum < 0) {
            logger.warn("Не достаточно средств на счете {} пользователя {}", transferFromUser.getId(), transferFromUser.getName());
            return false;
        }
        if (transferFromUser.getId() == transferToUser.getId()) {
            logger.warn("Невозможно выполнить перевод самому себе");
            return false;
        }
        return true;
    }

    private void getUsersAndSum() {
        sum = (long) (Math.random() * 2000 + 1);
        transferFromUser = service.getRandomUser(33);
        transferToUser = service.getRandomUser(33);
    }

    private void transfer() {
        transferFromUser.setBalance(transferFromUser.getBalance() - sum);
        transferToUser.setBalance(transferToUser.getBalance() + sum);
        logger.info("Успешный перевод на сумму {} от {} к {}! Остаток: {}-{}, {}-{}",
                sum, transferFromUser.getName(), transferToUser.getName(),
                transferFromUser.getName(), transferFromUser.getBalance(), transferToUser.getName(), transferToUser.getBalance());
    }
}
