package websocket;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLUserDAO;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameId, String authToken, Session session) throws DataAccessException {
        String username = new SQLAuthTokenDAO().getAuthToken(authToken).getUsername();
        if (!connections.containsKey(gameId)) {
            connections.get(gameId).add(new Connection(username, session));
        }
    }
}
