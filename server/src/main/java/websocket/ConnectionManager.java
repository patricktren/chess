package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.AuthToken;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void broadcast(Integer gameId, String excludeVisitorName, ServerMessage notification) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection : connections.get(gameId)) {
            if (connection.session.isOpen()) {
                if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)
                        && !connection.username.equals(excludeVisitorName)) {
                    connection.send(notification.toString());
                }
                else if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)
                        && notification.getMessage() == null
                        && connection.username.equals(excludeVisitorName)) {
                    connection.send(notification.toString());
                }
                else if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)
                        && notification.getMessage() != null) {
                    connection.send(notification.toString());
                }
//                connection.send(new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "ree", null, null).toString());
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
        Game game = new SQLGameDAO().getGame(command.getGameID());
        // get player color
        ChessGame.TeamColor playerColor = null;
        if (game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (game.blackUsername() != null && game.blackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        // add connection
        if (connections.containsKey(command.getGameID())) {
            connections.get(command.getGameID()).add(new Connection(username, command.getAuthToken(), session, playerColor));
        }
        else {
            ArrayList<Connection> newConnectionList = new ArrayList<>();
            newConnectionList.add(new Connection(username, command.getAuthToken(), session, playerColor));
            connections.put(command.getGameID(), newConnectionList);
        }
        // display message
        String message = "";
        if (command.isPlayer) {
            message = String.format("%s has joined the game as the %s player", username, playerColor.toString().toLowerCase());
        }
        else {
            message = String.format("%s has joined the game as an observer", username);
        }
        broadcast(command.getGameID(), username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, playerColor));
    }
    public void remove(UserGameCommand command) throws DataAccessException, IOException {
        var newConnectionList = new ArrayList<Connection>();
        for (Connection connection : connections.get(command.getGameID())) {
            if (!connection.authToken.equals(command.getAuthToken())) {
                newConnectionList.add(new Connection(connection.username, connection.authToken, connection.session, connection.playerColor));
            }
        }
        connections.put(command.getGameID(), newConnectionList);

        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        String message = String.format("%s has left the game", username);
        broadcast(command.getGameID(), username, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, null));
    }
    public void printGame(UserGameCommand command) throws IOException, DataAccessException {
        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        Game game = new SQLGameDAO().getGame(command.getGameID());
        ChessGame.TeamColor playerColor = null;
        if (game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (game.blackUsername() != null && game.blackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        broadcast(command.getGameID(), "", new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game, playerColor));
    }
}
