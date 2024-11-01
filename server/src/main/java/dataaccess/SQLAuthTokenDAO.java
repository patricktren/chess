package dataaccess;

import model.AuthToken;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class SQLAuthTokenDAO implements AuthTokenDAO{
    @Override
    public void createAuthToken(AuthToken authToken) throws DataAccessException {
        String sql_statement = "INSERT INTO auth_tokens (auth_token, username) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement,
                    Statement.RETURN_GENERATED_KEYS);
            // set the values to insert
            preparedStatement.setString(1, authToken.getToken());
            preparedStatement.setString(2, authToken.username());
            // execute
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public AuthToken getAuthToken(String authToken) throws DataAccessException {
        String sql_statement = "SELECT auth_token, username FROM auth_tokens WHERE auth_token = " + authToken;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                String authTokenResult = resultSet.getString("auth_token");
                String usernameResult = resultSet.getString("username");

                return new AuthToken(authTokenResult, usernameResult);
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String sql_statement = "DELETE FROM auth_tokens WHERE auth_token = " + authToken;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void clearAuthTokens() throws DataAccessException {
        String sql_statement = "DELETE FROM auth_tokens";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }
}
