package dataaccess;

import model.AuthToken;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class SQLAuthTokenDAO implements AuthTokenDAO{
    @Override
    public void createAuthToken(AuthToken authToken) throws DataAccessException {
        String sqlStatement = "INSERT INTO auth_tokens (auth_token, username) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
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
        if (authToken == null || authToken.isBlank()) {
            throw new DataAccessException("Error: authToken is empty");
        }
        String sqlStatement = "SELECT auth_token, username FROM auth_tokens WHERE auth_token = '" + authToken + "';";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet != null && resultSet.next()) {
                String authTokenResult = resultSet.getString("auth_token");
                String usernameResult = resultSet.getString("username");

                return new AuthToken(authTokenResult, usernameResult);
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
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String sqlStatement = "DELETE FROM auth_tokens WHERE auth_token = '" + authToken + "';";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void clearAuthTokens() throws DataAccessException {
        String sqlStatement = "DELETE FROM auth_tokens";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }
}
