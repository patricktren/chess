package service;

import dataaccess.MemoryDatabase;
import exception.ResponseException;
import model.User;
import org.junit.jupiter.api.*;
import protocol.RegisterRequest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {
    private static MemoryDatabase database;

    private static User newUser;

    @BeforeAll
    public static void init() {
        database = new MemoryDatabase();
    }

    @Test
    public void registerBasicUser() {
        try {
            newUser = new User("coolsammyo", "1cat2cat", "ree@gmail.com");
            var registerRequest = new RegisterRequest("", "1cat2cat", "ree@gmail.com");

            String expectedResult = newUser.getUsername();
            var actualResult = new RegisterService(database).registerUser(registerRequest).username();

            Assertions.assertEquals(expectedResult, actualResult);
        }
        catch (ResponseException er) {
            return;
        }
    }

}
