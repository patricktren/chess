package dataaccess;
import com.mysql.cj.protocol.Resultset;
import model.User;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO{
    @Override
    public void createUser(User user) throws DataAccessException {
        String sql_statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement,
                    Statement.RETURN_GENERATED_KEYS);

            // set the values to insert
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            // execute
            preparedStatement.executeUpdate();

            // return the username
            var resultSet = preparedStatement.getGeneratedKeys();
            var username = "";
            if (resultSet.next()) {
                username = resultSet.getString(1);
            }
            System.out.println(username);
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public User getUser(String username) throws DataAccessException {
        String sql_statement = "SELECT username, password, email FROM users";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                String usernameResult = resultSet.getString("username");
                String passwordResult = resultSet.getString("username");
                String emailResult = resultSet.getString("username");

                return new User(usernameResult, passwordResult, emailResult);
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String sql_statement = "DELETE FROM users";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }
}
