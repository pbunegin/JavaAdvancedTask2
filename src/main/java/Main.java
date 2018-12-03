import DAO.UserDao;
import DAO.UserDaoImpl;
import service.Service;
import service.ServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Service service = new ServiceImpl(userDao);
        System.out.println(service.getAllUsers());

    }
}
