package service;

import chess.ChessGame;
import dataaccess.MemoryDatabase;
import exception.ResponseException;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameTests {
    private static MemoryDatabase database;

    @BeforeEach
    public void init() {
        database = new MemoryDatabase();
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        CreateGameResponse expectedResponse = new CreateGameResponse(1010);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void createGameInvalidAuth() throws ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest("", "my new game");

        Assertions.assertThrows(ResponseException.class, () -> {
            CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);
        });
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        // join game
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResponse.authToken(), ChessGame.TeamColor.WHITE, 1010);
        JoinGameResponse joinGameResponse = new JoinGameService(database).joinGame(joinGameRequest);

        JoinGameResponse expectedResponse = new JoinGameResponse();

        // assert
        Assertions.assertEquals(expectedResponse, joinGameResponse);
    }

    @Test
    public void joinGameInvalidAuth() throws ResponseException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        // join game
        JoinGameRequest joinGameRequest = new JoinGameRequest("", ChessGame.TeamColor.WHITE, 1010);

        // assert
        Assertions.assertThrows(ResponseException.class, () -> {
            JoinGameResponse joinGameResponse = new JoinGameService(database).joinGame(joinGameRequest);
        });

    }

    @Test
    public void getGamesSuccess() throws ResponseException {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game(1010, "my new game", null, null, new ChessGame()));


        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        // get games
        GetGamesRequest getGamesRequest = new GetGamesRequest(registerResponse.authToken());
        GetGamesResponse getGamesResponse = new GetGamesService(database).getGames(getGamesRequest);

        // assert
        GetGamesResponse expectedResponse = new GetGamesResponse(games);
        Assertions.assertIterableEquals(expectedResponse.games(), getGamesResponse.games());

    }

    @Test
    public void getGamesInvalidAuth() throws ResponseException {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game(1010, "my new game", null, null, new ChessGame()));


        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResponse registerResponse = new RegisterService(database).registerUser(registerRequest);

        // create game
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "my new game");
        CreateGameResponse actualResponse = new CreateGameService(database).createGame(createGameRequest);

        // assert
        Assertions.assertThrows(ResponseException.class, () -> {
            GetGamesRequest getGamesRequest = new GetGamesRequest("");
            GetGamesResponse getGamesResponse = new GetGamesService(database).getGames(getGamesRequest);
        });
    }
}
