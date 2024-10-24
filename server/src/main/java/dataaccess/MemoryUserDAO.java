package dataaccess;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    Map<String, User> userMap = new HashMap<>();
    public MemoryUserDAO() {}

    @Override
    // create the user if they don't already exist
    public void createUser(User user) {
        if (!userMap.containsKey(user.getUsername())) {
            userMap.put(user.getUsername(), user);
        }
    }

    @Override
    // return the user if they exist, null if they don't
    public User getUser(String username) {
        return userMap.getOrDefault(username, null);
    }

    @Override
    // update the user if they exist
    public void updateUser(User user) {
        if (!userMap.containsKey(user.getUsername())) {
            userMap.put(user.getUsername(), user);
        }
    }

    @Override
    // delete the user if they exist
    public void deleteUser(String username) {
        userMap.remove(username);
    }

    @Override
    public void clearUsers() {
        userMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(userMap, that.userMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userMap);
    }
}
