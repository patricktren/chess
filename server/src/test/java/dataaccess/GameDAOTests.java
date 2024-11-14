package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class GameDAOTests {
    private static SQLDatabase database;
    private static SQLGameDAO sqlGameDAO;

    private static RegisterResponse registerResponse;
    private static User newUser;

    @BeforeEach
    public void init() throws DataAccessException, ResponseException {
        database = new SQLDatabase();
        sqlGameDAO = (SQLGameDAO) database.getGameDAO();
        database.clearDatabase();

        // register user
        newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        registerResponse = new RegisterService(database).registerUser(registerRequest);
    }

    @Test
    public void createGameSuccess() throws ResponseException, DataAccessException {
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));

        Assertions.assertEquals(gameID, sqlGameDAO.getGame(gameID).gameID());
    }

    @Test
    public void createGameInvalidGameName() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlGameDAO.createGame(new Game(null, null, null, null, new ChessGame()));        });
    }

    @Test
    public void joinGameSuccess() throws ResponseException, DataAccessException {
        // create game
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));

        // join game
        Game expectedResponse = new Game(gameID, "newGame1", newUser.username(), null, null);
        sqlGameDAO.updateGame(expectedResponse);

        // get game
        Game joinGameResponse = sqlGameDAO.getGame(gameID);

        // assert
        Assertions.assertEquals(expectedResponse, joinGameResponse);
    }

    @Test
    public void joinGameInvalidGameID() throws ResponseException, DataAccessException {
        // create game
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));

        // join game
        Game expectedResponse = new Game(null, "newGame1", newUser.username(), null, new ChessGame());
        sqlGameDAO.updateGame(expectedResponse);

        // get game
        Game joinGameResponse = sqlGameDAO.getGame(gameID);

        // assert
        Assertions.assertNotEquals(expectedResponse, joinGameResponse);
    }

    @Test
    public void getGamesSuccess() throws ResponseException, DataAccessException {
        ArrayList<Game> expectedGames = new ArrayList<>();
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));
        expectedGames.add(sqlGameDAO.getGame(gameID));

        ArrayList<Game> actualGames = sqlGameDAO.getGames(registerResponse.authToken());

        Assertions.assertEquals(expectedGames, actualGames);

    }

    @Test
    public void getGamesLostDatabaseConnection() throws DataAccessException {
        sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));
        sqlGameDAO = null;
        // assert
        Assertions.assertThrows(NullPointerException.class, () -> {
            ArrayList<Game> actualGames = sqlGameDAO.getGames("");
        });
    }
    @Test
    public void updateGameSuccess() throws DataAccessException {
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));

        // join game
        Game expected = new Game(gameID, "newGame1", null, newUser.username(), null);
        sqlGameDAO.updateGame(expected);

        // get game
        Game actual = sqlGameDAO.getGame(gameID);

        // assert
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void updateGameIncorrectGameID() throws DataAccessException {
        Integer gameID = sqlGameDAO.createGame(new Game(null, "newGame1", null, null, new ChessGame()));

        // join game
        Game expected = new Game(gameID, "newGame1", null, newUser.username(), null);
        sqlGameDAO.updateGame(expected);

        // get game
        Game actual = sqlGameDAO.getGame(Math.toIntExact(Math.round(Math.random())));

        // assert
        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    public void clearGamesSuccess() throws DataAccessException {
        sqlGameDAO.clearGames();
        String sqlStatement = "SELECT * FROM games;";
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
