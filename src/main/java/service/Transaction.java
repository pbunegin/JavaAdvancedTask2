package service;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

public class Transaction implements Runnable {
    private User transferFromUser;
    private User transferToUser;
    private long sum;
    private Logger logger = LoggerFactory.getLogger(Transaction.class);


    public Transaction(User transferFromUser, User transferToUser, long sum) {
        this.transferFromUser = transferFromUser;
        this.transferToUser = transferToUser;
        this.sum = sum;
    }

    @Override
    public void run() {
        int id1 = transferFromUser.getId();
        int id2 = transferToUser.getId();
        Lock lock1 = transferFromUser.getLock();
        Lock lock2 = transferToUser.getLock();
        if (id1 == id2) {
            logger.warn("Невозможно выполнить перевод самому себе");
            return;
        } else if (id1 < id2) {
            Lock tempLock = lock1;
            lock1 = lock2;
            lock2 = tempLock;
        }

        lock1.lock();
        lock2.lock();
        try {
            transfer(transferFromUser, transferToUser, sum);
        } finally {
            lock1.unlock();
            lock2.unlock();
        }
    }

    private void transfer(User transferFromUser, User transferToUser, long sum) {
        if (transferFromUser.getBalance() - sum < 0) {
            logger.warn("Не достаточно средств на счете {} пользователя {}", transferFromUser.getId(), transferFromUser.getName());
            return;
        }
        transferFromUser.setBalance(transferFromUser.getBalance() - sum);
        transferToUser.setBalance(transferToUser.getBalance() + sum);
        logger.info("Успешный перевод на сумму {} от пользователя {} пользователю {}!\n" +
                "Остаток на счете {} пользователя {}: {}\n" +
                "Остаток на счете {} пользователя {}: {}", sum, transferFromUser.getName(), transferToUser.getName(),
                transferFromUser.getId(), transferFromUser.getName(), transferFromUser.getBalance(),
                transferToUser.getId(), transferToUser.getName(), transferToUser.getBalance());
    }
}
