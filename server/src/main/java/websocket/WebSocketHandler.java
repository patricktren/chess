package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
        }
    }
    private void connect(UserGameCommand command, Session session) throws DataAccessException {
        connections.add(command.getGameID(), command.getAuthToken(), session);
    }
    private void disconnect(UserGameCommand command) {
        connections.remove(command.getGameID(), command.getAuthToken());
    }
}
