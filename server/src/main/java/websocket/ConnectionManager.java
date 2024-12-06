package websocket;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLUserDAO;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void broadcast(Integer gameId, String excludeVisitorName, ServerMessage notification) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection : connections.get(gameId)) {
            if (connection.session.isOpen()) {
                if (connection.username.equals(excludeVisitorName)) {
                    connection.send(notification.toString());
                }
            } else {
                removeList.add(connection);
            }
        }
        // Clean up any connections that were left open.
        for (Connection connection : removeList) {
            connections.remove(gameId, connection.authToken);
        }
    }

    public void add(Integer gameId, String authToken, Session session) throws DataAccessException, IOException {
        String username = new SQLAuthTokenDAO().getAuthToken(authToken).getUsername();
        if (connections.containsKey(gameId)) {
            connections.get(gameId).add(new Connection(username, authToken, session));
        }
        else {
            ArrayList<Connection> newConnectionList = new ArrayList<>();
            newConnectionList.add(new Connection(username, authToken, session));
            connections.put(gameId, newConnectionList);
        }
        String message = String.format("%s has joined the game", username);
        broadcast(gameId, username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null));
    }
    public void remove(Integer gameId, String authToken) throws DataAccessException, IOException {
        var newConnectionList = new ArrayList<Connection>();
        for (Connection connection : connections.get(gameId)) {
            if (!connection.authToken.equals(authToken)) {
                newConnectionList.add(new Connection(connection.username, connection.authToken, connection.session));
            }
        }
        connections.put(gameId, newConnectionList);

        String username = new SQLAuthTokenDAO().getAuthToken(authToken).getUsername();
        String message = String.format("%s has left the game", username);
        broadcast(gameId, username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null));
    }
}
