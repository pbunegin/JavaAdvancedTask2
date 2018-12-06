package utilities;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtility {
    private static DBUtility DBUtility;
    private String pathAccount;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtility.class);

    private DBUtility(String pathAccount) {
        this.pathAccount = pathAccount;
    }

    public static DBUtility getInstance(String pathAccount) {
        if (DBUtility == null) {
            DBUtility = new DBUtility(pathAccount);
        }
        return DBUtility;
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        try {
            Files.walk(Paths.get(pathAccount), 1)
                    .filter(path -> Files.isRegularFile(path))
                    .forEach(file -> users.add(getUserByFile(file)));
        } catch (IOException e) {
            LOGGER.error("Ошибка при попытке получить всех пользователей: " + e.getMessage());
        }
        return users;
    }

    private User getUserByFile(Path file) {
        User user = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            user = (User) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Ошибка при попытке получить пользователя: {}", e.getMessage());
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
            LOGGER.error("Ошибка записи в файл при попытке создания/обновления пользователя: {}", e.getMessage());
        }
    }

    public void deleteUser(int id) {
        try {
            Files.delete(Paths.get(pathAccount + "\\" + id));
        } catch (NoSuchFileException e) {
            LOGGER.error("Ошибка при попытке удалить файл. Файл не существует. - {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Ошибка при попытке удалить файл: {}", e.getMessage());
        }
    }
}
