package protocol;

public record CreateGameRequest(String authToken, String gameName) {
}
