package service;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

public class Transaction implements Runnable {
    private Service service;
    private int maxNumberTransactions;
    private User transferFromUser;
    private User transferToUser;
    private long sum;
    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Service service, int maxNumberTransactions) {
        this.service = service;
        this.maxNumberTransactions = maxNumberTransactions;
    }

    @Override
    public void run() {
        while (service.getCountTransaction() < maxNumberTransactions) {
            getUsersAndSum();
            if (!checkUsers()){
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
                if (checkTransaction()) {
                    transfer();
                } else {
                    service.decrementCountTransaction();
                }
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    private boolean checkUsers() {
        if (transferFromUser == null || transferToUser == null) {
            return false;
        }
        if (transferFromUser.getId() == transferToUser.getId()) {
            LOGGER.warn("Невозможно выполнить перевод самому себе. Счет {}, пользователь {}",
                    transferFromUser.getId(), transferFromUser.getName());
            return false;
        }
        return true;
    }

    private boolean checkTransaction() {
        if (transferFromUser.getBalance() - sum < 0) {
            LOGGER.warn("Недостаточно средств на счете {} пользователя {}", transferFromUser.getId(), transferFromUser.getName());
            return false;
        }
        return true;
    }

    private void getUsersAndSum() {
        sum = (long) (Math.random() * 2000 + 1);
        transferFromUser = service.getRandomUser();
        transferToUser = service.getRandomUser();
    }

    private void transfer() {
        transferFromUser.setBalance(transferFromUser.getBalance() - sum);
        transferToUser.setBalance(transferToUser.getBalance() + sum);
        LOGGER.info("Успешный перевод на сумму {} от {} к {}! Остаток: {}-{}, {}-{}",
                sum, transferFromUser.getName(), transferToUser.getName(),
                transferFromUser.getName(), transferFromUser.getBalance(), transferToUser.getName(), transferToUser.getBalance());
    }
}
