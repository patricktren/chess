package websocket;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLUserDAO;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
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
                if (!connection.username.equals(excludeVisitorName)) {
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

    public void add(UserGameCommand command, Session session) throws DataAccessException, IOException {
        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        if (connections.containsKey(command.getGameID())) {
            connections.get(command.getGameID()).add(new Connection(username, command.getAuthToken(), session, command.getPlayerColor()));
        }
        else {
            ArrayList<Connection> newConnectionList = new ArrayList<>();
            newConnectionList.add(new Connection(username, command.getAuthToken(), session, command.getPlayerColor()));
            connections.put(command.getGameID(), newConnectionList);
        }
        String message = String.format("%s has joined the game", username);
        broadcast(command.getGameID(), username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, command.getPlayerColor()));
    }
    public void remove(UserGameCommand command) throws DataAccessException, IOException {
        var newConnectionList = new ArrayList<Connection>();
        for (Connection connection : connections.get(command.getGameID())) {
            if (!connection.authToken.equals(command.getAuthToken())) {
                newConnectionList.add(new Connection(connection.username, connection.authToken, connection.session, command.getPlayerColor()));
            }
        }
        connections.put(command.getGameID(), newConnectionList);

        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        String message = String.format("%s has left the game", username);
        broadcast(command.getGameID(), username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, command.getPlayerColor()));
    }
}
