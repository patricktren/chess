package dataaccess;

import exception.ResponseException;
import model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.getConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTests {
    private static SQLDatabase database;
    private static SQLUserDAO sqlUserDAO;

    private static RegisterResponse registerResponse;
    private static User newUser;

    @BeforeEach
    public void init() throws DataAccessException, ResponseException {
        database = new SQLDatabase();
        sqlUserDAO = (SQLUserDAO) database.getUserDAO();
        database.clearDatabase();

        // register user
        newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        sqlUserDAO.createUser(newUser);
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        Assertions.assertNotNull(sqlUserDAO.getUser(newUser.username()));
    }

    @Test
    public void createUserNullUsername() {
        User invalidUser = new User(null, "1cat2cat", "ree@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlUserDAO.createUser(invalidUser);
        });
    }

    @Test
    public void getUserSuccess() throws ResponseException, DataAccessException {
        Assertions.assertEquals(newUser, sqlUserDAO.getUser(newUser.username()));
    }
    @Test
    public void getUserDoesNotExist() throws DataAccessException {
        Assertions.assertNull(sqlUserDAO.getUser(""));
    }

    @Test
    public void clearUsersSuccess() throws DataAccessException {
        sqlUserDAO.clearUsers();
        String sqlStatement = "SELECT * FROM users;";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            // Assert
            Assertions.assertFalse(resultSet.next());
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }
}
