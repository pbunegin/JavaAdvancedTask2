package service;

import entity.User;

public class Transaction implements Runnable {
    private User transferFromUser;
    private User transferToUser;
    private long sum;

    public Transaction(User transferFromUser, User transferToUser, long sum) {
        this.transferFromUser = transferFromUser;
        this.transferToUser = transferToUser;
        this.sum = sum;
    }

    @Override
    public void run() {
        try {
            if (transferFromUser.getLock().tryLock()){
                if (transferToUser.getLock().tryLock()){
                    transfer(transferFromUser, transferToUser, sum);
                }
            }
        } finally {
            transferFromUser.getLock().unlock();
            transferToUser.getLock().unlock();
        }
    }

    private void transfer(User user1, User user2, long sum) {
        if (user1.getBalance() - sum < 0) {
            System.out.printf("Не достаточно средств на счете %d пользователя %s\n", user1.getId(), user1.getName());
            return;
        }
        user1.setBalance(user1.getBalance() - sum);
        user1.setBalance(user1.getBalance() + sum);
        System.out.printf("Успешная транзакция! Остаток на счете %d пользователя %s: %d\n", user1.getId(), user1.getName(),user1.getBalance());
        System.out.printf("Успешная транзакция! Остаток на счете %d пользователя %s: %d\n", user2.getId(), user2.getName(),user2.getBalance());
    }
}
