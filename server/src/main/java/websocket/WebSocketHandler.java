package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLGameDAO;
import model.Game;
import model.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        boolean noErrors = true;
        // handle errors
        try {
            String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
            if (username == null) {
                throw new IOException("Error: not logged in.");
            }
            Game game = new SQLGameDAO().getGame(command.getGameID());
            if (game == null) {
                throw new IOException("Error: game doesn't exist.");
            }
        }
        catch (Throwable er) {
            noErrors = false;
            try {
                sendErrorMessage(session, er.getMessage());
            }
            // handle error when we fail to send the error message
            catch (Throwable failedToSendError) {
                System.out.println(failedToSendError.getMessage());
            }
        }
        if (noErrors) {
            try {
                switch (command.getCommandType()) {
                    case CONNECT -> connect(command, session);
                    case MAKE_MOVE -> printGame(command);
                    case LEAVE -> disconnect(command);
                }
            } catch (Throwable er) {
                System.out.println(er.getMessage());
            }
        }
    }
    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        connections.add(command, session);
        connections.printGame(new UserGameCommand(UserGameCommand.CommandType.CONNECT, command.getAuthToken(), command.getGameID()));
    }
    private void disconnect(UserGameCommand command) throws IOException, DataAccessException {
        connections.remove(command);
    }

    private void printGame(UserGameCommand command) throws IOException, DataAccessException {
        connections.printGame(command);
    }

    private void sendErrorMessage(Session session, String errorMessage) throws IOException {
        session.getRemote().sendString(errorMessage);
    }
}
