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
        transferFromUser.getLock().lock();
        transferToUser.getLock().lock();
        try {
            transfer(transferFromUser, transferToUser, sum);
        } finally {
            transferFromUser.getLock().unlock();
            transferToUser.getLock().unlock();
        }
    }

    private void transfer(User transferFromUser, User transferToUser, long sum) {
        if (transferFromUser.getBalance() - sum < 0) {
            System.out.printf("\nНе достаточно средств на счете %d пользователя %s", transferFromUser.getId(), transferFromUser.getName());
            return;
        }
        transferFromUser.setBalance(transferFromUser.getBalance() - sum);
        transferToUser.setBalance(transferToUser.getBalance() + sum);
        System.out.printf("\nУспешный перевод от пользователя %s пользователю %s!", transferFromUser.getName(),transferToUser.getName());
        System.out.printf("\nОстаток на счете %d пользователя %s: %d", transferFromUser.getId(), transferFromUser.getName(),transferFromUser.getBalance());
        System.out.printf("\nОстаток на счете %d пользователя %s: %d", transferToUser.getId(), transferToUser.getName(),transferToUser.getBalance());
    }
}
