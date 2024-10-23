package model;

public record User(String username, String password, String email) {

    public String getUsername() {
        return username;
    }
}
