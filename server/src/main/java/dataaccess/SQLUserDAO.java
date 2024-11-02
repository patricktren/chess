package dataaccess;
import com.mysql.cj.protocol.Resultset;
import model.User;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO{
    @Override
    public void createUser(User user) throws DataAccessException {
        String sqlStatement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
                    Statement.RETURN_GENERATED_KEYS);

            // set the values to insert
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            // execute
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public User getUser(String username) throws DataAccessException {
        String sqlStatement = "SELECT username, password, email FROM users WHERE username = '" + username + "';";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet != null && resultSet.next()) {
                String usernameResult = resultSet.getString("username");
                String passwordResult = resultSet.getString("password");
                String emailResult = resultSet.getString("email");

                return new User(usernameResult, passwordResult, emailResult);
            }
            else {
                return null;
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String sqlStatement = "DELETE FROM users";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }
}
