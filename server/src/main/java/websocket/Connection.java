package websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public String authToken;
    public Session session;
    public ChessGame.TeamColor playerColor;

    public Connection(String username, String authToken, Session session, ChessGame.TeamColor playerColor) {
        this.username = username;
        this.authToken = authToken;
        this.session = session;
        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
