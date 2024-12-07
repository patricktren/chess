package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import server.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    ServerFacade server;
    NotificationHandler notificationHandler;
    String url;
    Session session;
    String authToken;
    String username;
    public WebSocketFacade(ServerFacade server, NotificationHandler notificationHandler, String authToken, String username) throws ResponseException {
        try {
            this.server = server;
            this.notificationHandler = notificationHandler;

            url = server.getServerUrl().replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        }

        catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void enterGame(String username, ChessGame.TeamColor playerColor) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, server.getAuthToken(), server.getCurrGameId());
            if (playerColor == null) {
                command.isPlayer = false;
            }
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String username) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, server.getAuthToken(), server.getCurrGameId());
            server.resetCurrGameId();
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String paramStr, String authToken) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, server.getCurrGameId());
            command.setMove(paramStr);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public Integer parseLetterToInt(char character) {
        return switch (String.valueOf(character)) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> 0;
        };
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
