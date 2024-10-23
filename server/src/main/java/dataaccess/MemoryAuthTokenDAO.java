package dataaccess;

import model.AuthToken;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthTokenDAO implements AuthTokenDAO{
    Map<String, AuthToken> authTokenMap = new HashMap<>();

    public MemoryAuthTokenDAO() {}

    @Override
    public void createAuthToken(AuthToken authToken) {
        authTokenMap.put(authToken.getToken(), authToken);
    }

    @Override
    public AuthToken getAuthToken(String token) {
        return authTokenMap.get(token);
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) {
        authTokenMap.remove(authToken.getToken());
    }
}
