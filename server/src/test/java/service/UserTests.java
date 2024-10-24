package service;

import dataaccess.MemoryDatabase;
import exception.ResponseException;
import model.User;
import org.junit.jupiter.api.*;
import protocol.LoginRequest;
import protocol.RegisterRequest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {
    private static MemoryDatabase database;

    @BeforeEach
    public void init() {
        database = new MemoryDatabase();
    }

    @Test
    public void registerUserSuccess() throws ResponseException {
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());

        String expectedResult = newUser.getUsername();
        String actualResult = new RegisterService(database).registerUser(registerRequest).username();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void registerUserInvalidUsername() {
        var registerRequest = new RegisterRequest("", "ree", "ree@gmail.com");
        Assertions.assertThrows(ResponseException.class, () -> {
            var actualResult = new RegisterService(database).registerUser(registerRequest);
        });
    }

    @Test
    public void loginUserDoesNotExist() {
        var loginRequest = new LoginRequest("hello", "ree");
        Assertions.assertThrows(ResponseException.class, () -> {
            var actualResult = new LoginService(database).login(loginRequest);
        });
    }

    @Test
    public void loginUserExists() throws ResponseException {
        // register user
        User newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
        var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        String username = new RegisterService(database).registerUser(registerRequest).username();

        // assertions
        String expectedResultUsername = "coolsammyo";

        // login
        var loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        var loginResult = new LoginService(database).login(loginRequest);

        // assert
        Assertions.assertFalse(loginResult.token().isBlank());
        Assertions.assertEquals(loginResult.username(), expectedResultUsername);
    }
}
