package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session);
            }
        }
        catch (Throwable er) {
            System.out.println(er.getMessage());
        }
    }
    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        connections.add(command, session);
    }
    private void disconnect(UserGameCommand command) throws IOException, DataAccessException {
        connections.remove(command);
    }
}
