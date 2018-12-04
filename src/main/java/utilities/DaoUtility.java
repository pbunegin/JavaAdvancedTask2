package utilities;

import entity.User;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DaoUtility {
    private static DaoUtility daoUtility;
    private String pathAccount;

    private DaoUtility(String pathAccount) {
        this.pathAccount = pathAccount;
    }

    public static DaoUtility getInstance(String pathAccount) {
        if (daoUtility == null) {
            daoUtility = new DaoUtility(pathAccount);
        }
        return daoUtility;
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(pathAccount), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    users.add(getUserByFile(file));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("Ошибка при попытке получить всех пользователей: " + e.getMessage());
        }
        return users;
    }

    private User getUserByFile(Path file) {
        User user = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            user = (User) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при попытке получить пользователя: " + e.getMessage());
        }
        return user;
    }

    public User getUserById(int id) {
        return getUserByFile(Paths.get(pathAccount + "\\" + id));
    }


    public void createOrUpdateUser(User user) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathAccount + "\\" + user.getId()))) {
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл при попытке создания/обновления пользователя: " + e.getMessage());
        }
    }

    public void deleteUser(int id) {
        try {
            Files.delete(Paths.get(pathAccount + "\\" + id));
        } catch (NoSuchFileException e) {
            System.out.println("Ошибка при попытке удалить файл. Файл не существует. " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка при попытке удалить файл: " + e.getMessage());
        }
    }
}