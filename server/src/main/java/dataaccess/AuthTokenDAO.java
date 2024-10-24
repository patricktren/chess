package dataaccess;

import model.AuthToken;

public interface AuthTokenDAO {
    public void createAuthToken(AuthToken authToken);
    public AuthToken getAuthToken(String token);
//    public void updateAuthToken(AuthToken authToken);
    public void deleteAuthToken(AuthToken authToken);
    public void clearAuthTokens();
}
