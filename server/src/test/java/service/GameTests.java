package service;

import dataaccess.MemoryDatabase;
import exception.ResponseException;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.CreateGameRequest;
import protocol.CreateGameResponse;
import protocol.RegisterRequest;
import protocol.RegisterResponse;

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
}
