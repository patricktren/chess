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
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, server.getAuthToken(), server.getCurrGameId(), playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
