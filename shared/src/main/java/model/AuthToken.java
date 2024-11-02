package model;

public record AuthToken(String authToken, String username) {
    public String getToken() {
        return authToken;
    }
    public String getUsername() {
        return username;
    }
}
