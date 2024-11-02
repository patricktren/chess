package dataaccess;

import exception.ResponseException;
import model.AuthToken;
import model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.GetGamesRequest;
import protocol.GetGamesResponse;
import service.GetGamesService;
import service.RegisterService;
import service.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static dataaccess.DatabaseManager.getConnection;

public class AuthTokenDAOTests {
    private static SQLDatabase database;

    Service service;
    SQLAuthTokenDAO sqlAuthTokenDAO;

    String validAuthTokenStr;

    @BeforeEach
    public void init() throws DataAccessException {
        database = new SQLDatabase();
        database.clearDatabase();
        sqlAuthTokenDAO = database.sqlAuthTokenDAO;
        service = new RegisterService(database);

        validAuthTokenStr = UUID.randomUUID().toString();
        // create in sql
        sqlAuthTokenDAO.createAuthToken(new AuthToken(validAuthTokenStr, "user1"));
    }

    @Test
    public void createAuthTokenSuccess() throws DataAccessException {
        AuthToken expectedAuthToken = new AuthToken(validAuthTokenStr, "user1");
        // get created authToken
        AuthToken returnedAuthToken = sqlAuthTokenDAO.getAuthToken(validAuthTokenStr);

        Assertions.assertEquals(expectedAuthToken, returnedAuthToken);
    }

    @Test
    public void createAuthTokenFailure() throws DataAccessException {
        // assert
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthTokenDAO.createAuthToken(new AuthToken(null, "user2"));
        });
    }

    @Test
    public void getAuthTokenSuccess() throws DataAccessException {
        String expectedAuthTokenStr = new AuthToken(validAuthTokenStr, "user1").authToken();
        // get created authToken
        String returnedAuthTokenStr = sqlAuthTokenDAO.getAuthToken(validAuthTokenStr).authToken();

        Assertions.assertEquals(expectedAuthTokenStr, returnedAuthTokenStr);
    }
    @Test
    public void getAuthTokenFailure() throws DataAccessException {
        // assert
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthTokenDAO.createAuthToken(new AuthToken(null, "user2"));
        });
    }

    @Test
    public void deleteAuthTokenSuccess() throws DataAccessException {
        sqlAuthTokenDAO.deleteAuthToken(validAuthTokenStr);
        String expectedAuthTokenStr = null;
        // get created authToken
        var returnedAuthTokenStr = sqlAuthTokenDAO.getAuthToken(validAuthTokenStr);

        Assertions.assertEquals(expectedAuthTokenStr, returnedAuthTokenStr);
    }

    @Test
    public void deleteAuthTokenFailure() throws DataAccessException {
        // assert
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthTokenDAO.createAuthToken(new AuthToken(null, "user2"));
        });
    }

    @Test
    public void clearAuthTokenSuccess() throws DataAccessException {
        sqlAuthTokenDAO.clearAuthTokens();
        String sqlStatement = "SELECT * FROM auth_tokens;";
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
