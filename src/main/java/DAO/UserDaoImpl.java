package DAO;

import entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    private List<User> users;

    public UserDaoImpl() {
        users = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get("src\\main\\java\\accounts"),new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                        users.add((User) objectInputStream.readObject());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id)
                return user;
        }
        return null;
    }

    @Override
    public User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name))
                return user;
        }
        return null;
    }

    @Override
    public void createUser(int id, String name, long balance) {

    }

    @Override
    public void deleteUser(int id) {

    }
}
