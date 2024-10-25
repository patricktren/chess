package dataaccess;
import model.User;

public interface UserDAO {
    public void createUser(User user) throws DataAccessException;
    public User getUser(String username) throws DataAccessException;
    public void updateUser(User user) throws DataAccessException;
    public void deleteUser(String username) throws DataAccessException;
    public void clearUsers() throws DataAccessException;
}
