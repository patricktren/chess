package model;

public record AuthToken(String token, String username) {
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
}
