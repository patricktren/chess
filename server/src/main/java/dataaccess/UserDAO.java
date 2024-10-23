package dataaccess;
import model.User;

public interface UserDAO {
    public void createUser(User user);
    public User getUser(String username);
    public void updateUser(User user);
    public void deleteUser(String username);
}
