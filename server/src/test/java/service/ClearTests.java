package service;

import dataaccess.Database;
import dataaccess.MemoryDatabase;
import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.RegisterRequest;

public class ClearTests {
    private static Database database;

    @BeforeEach
    public void init() {
        database = new MemoryDatabase();
    }

    @Test
    public void clearDatabaseSuccess() throws ResponseException {
        var clearResult = new ClearService(database).clearDatabase();
        Database expectedResult = new MemoryDatabase();
        Assertions.assertEquals(expectedResult, database);
    }

    @Test
    public void clearDatabaseFailure() throws ResponseException {
        var clearResult = new ClearService(database).clearDatabase();
        // add a user so the database is no longer empty
        var registerRequest = new RegisterRequest("coolsammyo", "ree", "ree@gmail.com");
        var registerResult = new RegisterService(database).registerUser(registerRequest);

        Database expectedResult = new MemoryDatabase();
        Assertions.assertNotEquals(expectedResult, database);
    }


}
