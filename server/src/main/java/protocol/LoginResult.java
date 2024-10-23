package protocol;

import model.AuthToken;

public record LoginResult(String username, AuthToken authToken) {
}
