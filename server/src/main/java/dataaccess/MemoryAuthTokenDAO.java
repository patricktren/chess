package dataaccess;

import model.AuthToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthTokenDAO implements AuthTokenDAO{
    Map<String, AuthToken> authTokenMap = new HashMap<>();

    public MemoryAuthTokenDAO() {}

    @Override
    public void createAuthToken(AuthToken authToken) {
        authTokenMap.put(authToken.getToken(), authToken);
    }

    @Override
    public AuthToken getAuthToken(String authToken) {
        return authTokenMap.get(authToken);
    }

    @Override
    public void deleteAuthToken(String authToken) {
        authTokenMap.remove(authToken);
    }

    @Override
    public void clearAuthTokens() {
        authTokenMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        MemoryAuthTokenDAO that = (MemoryAuthTokenDAO) o;
        return Objects.equals(authTokenMap, that.authTokenMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authTokenMap);
    }
}
