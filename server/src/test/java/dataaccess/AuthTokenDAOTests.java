package dataaccess;

import org.junit.jupiter.api.BeforeEach;

public class AuthTokenDAOTests {
    private static SQLDatabase database;

    @BeforeEach
    public void init() throws DataAccessException {
        database = new SQLDatabase();
        database.clearDatabase();
    }

}
