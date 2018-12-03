import DAO.UserDao;
import DAO.UserDaoImpl;
import entity.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        System.out.println(userDao.getAllUsers());

    }
}
