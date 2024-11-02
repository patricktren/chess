package dataaccess;

import chess.ChessGame;
import dataaccess.Database;
import dataaccess.SQLDatabase;
import exception.ResponseException;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;
import service.CreateGameService;
import service.GetGamesService;
import service.JoinGameService;
import service.RegisterService;

import java.util.ArrayList;

public class GameDAOTests {
    private static SQLDatabase database;

    @BeforeEach
    public void init() throws DataAccessException {
        database = new SQLDatabase();
        database.clearDatabase();
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        Assertions.assertNotNull(actualResponse);
    }

    @Test
    public void createGameInvalidAuth() throws ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest("", "my new game");

        Assertions.assertThrows(ResponseException.class, () -> {
            CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        });
    }

    @Test
    public void joinGameSuccess() throws ResponseException, DataAccessException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        // get game id
        ArrayList<Game> gameList = database.getGameDAO().getGames(registerResponse.authToken());
        var gameID = gameList.get(0).gameID();

        // join game
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResponse.authToken(), ChessGame.TeamColor.WHITE, gameID);
        JoinGameResponse joinGameResponse = new JoinGameService(database).joinGame(joinGameRequest);

        JoinGameResponse expectedResponse = new JoinGameResponse();

        // assert
        Assertions.assertEquals(expectedResponse, joinGameResponse);
    }

    @Test
    public void joinGameInvalidAuth() throws ResponseException, DataAccessException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        // get game id
        ArrayList<Game> gameList = database.getGameDAO().getGames(registerResponse.authToken());
        var gameID = gameList.get(0).gameID();

        // join game
        JoinGameRequest joinGameRequest = new JoinGameRequest("", ChessGame.TeamColor.WHITE, gameID);

        // assert
        Assertions.assertThrows(ResponseException.class, () -> {
            JoinGameResponse joinGameResponse = new JoinGameService(database).joinGame(joinGameRequest);
        });
    }

    @Test
    public void getGamesSuccess() throws ResponseException, DataAccessException {

        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        // get game id
        ArrayList<Game> gameList = database.getGameDAO().getGames(registerResponse.authToken());
        var gameID = gameList.get(0).gameID();

        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game(gameID, "my new game", null, null, null));

        // get games
        GetGamesRequest getGamesRequest = new GetGamesRequest(registerResponse.authToken());
        GetGamesResponse getGamesResponse = new GetGamesService(database).getGames(getGamesRequest);

        // assert
        GetGamesResponse expectedResponse = new GetGamesResponse(games);
        Assertions.assertIterableEquals(expectedResponse.games(), getGamesResponse.games());

    }

    @Test
    public void getGamesInvalidAuth() throws ResponseException, DataAccessException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        // get game id
        ArrayList<Game> gameList = database.getGameDAO().getGames(registerResponse.authToken());
        var gameID = gameList.get(0).gameID();

        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game(gameID, "my new game", null, null, null));

        // assert
        Assertions.assertThrows(ResponseException.class, () -> {
            GetGamesRequest getGamesRequest = new GetGamesRequest("");
            GetGamesResponse getGamesResponse = new GetGamesService(database).getGames(getGamesRequest);
        });
    }
}
