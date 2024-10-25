package dataaccess;

import java.util.Objects;

public class MemoryDatabase implements Database{
    private final MemoryUserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;
    private final GameDAO gameDAO;

    public MemoryDatabase() {
        this.userDAO = new MemoryUserDAO();
        this.authTokenDAO = new MemoryAuthTokenDAO();
        this.gameDAO = new MemoryGameDAO() {
        };
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
    public GameDAO getGameDAO() {
        return gameDAO;
    }

    @Override
    public void clearDatabase() throws DataAccessException {
        userDAO.clearUsers();
        authTokenDAO.clearAuthTokens();
        gameDAO.clearGames();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryDatabase that = (MemoryDatabase) o;
        return Objects.equals(userDAO, that.userDAO) && Objects.equals(authTokenDAO, that.authTokenDAO) && Objects.equals(gameDAO, that.gameDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDAO, authTokenDAO, gameDAO);
    }
}
