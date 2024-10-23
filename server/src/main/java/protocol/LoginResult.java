package protocol;

import model.AuthToken;

public record LoginResult(String username, String token) {
}
