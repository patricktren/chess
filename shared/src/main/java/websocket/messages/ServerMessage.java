package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    Game game;
    ChessGame.TeamColor playerColor;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String message, Game game, ChessGame.TeamColor playerColor) {
        this.serverMessageType = type;
        this.message = message;
        this.game = game;
        this.playerColor = playerColor;
        if (type.equals(ServerMessageType.ERROR)) {
            this.errorMessage = message;
            this.message = null;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        return message;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
