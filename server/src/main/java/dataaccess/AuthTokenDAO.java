package dataaccess;

import model.AuthToken;

public interface AuthTokenDAO {
    public void createAuthToken(AuthToken authToken) throws DataAccessException;
    public AuthToken getAuthToken(String token) throws DataAccessException;
//    public void updateAuthToken(AuthToken authToken) throws DataAccessException;
    public void deleteAuthToken(String authToken) throws DataAccessException;
    public void clearAuthTokens() throws DataAccessException;
}
