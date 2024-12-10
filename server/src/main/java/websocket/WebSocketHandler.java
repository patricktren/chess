package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLAuthTokenDAO;
import dataaccess.SQLGameDAO;
import exception.Message;
import model.Game;
import model.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketError
    public void onError(Throwable err) throws Throwable {
        System.out.println("reee");
        throw err;
    }

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
        catch (Exception er) {
            noErrors = false;
            try {
                sendErrorMessage(session, er.getMessage());
            }
            // handle error when we fail to send the error message
            catch (Exception failedToSendError) {
                System.out.println(failedToSendError.getMessage());
            }
        }
        if (noErrors) {
            try {
                switch (command.getCommandType()) {
                    case CONNECT -> connect(command, session);
                    case MAKE_MOVE -> makeMove(command, session);
                    case LEAVE -> disconnect(command);
                    case RESIGN -> resign(command, session);
                }
            } catch (Exception er) {
                System.out.println(er.getMessage());
                try {
                    sendErrorMessage(session, er.getMessage());
                }
                catch (Exception doubleError) {
                    System.out.println(doubleError.getMessage());
                }
            }
        }
    }
    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {
        var game = new SQLGameDAO().getGame(command.getGameID());
        if (game.gameState().getGameOver()) {
            sendErrorMessage(session, "You may not resign because the game has already ended.");
            return;
        }

        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        // get playerColor
        ChessGame.TeamColor playerColor = null;
        if (game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (game.blackUsername() != null && game.blackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        // check if observer is trying to make a move
        if (!game.whiteUsername().equals(username) && !game.blackUsername().equals(username)) {
            command.isPlayer = false;
            sendErrorMessage(session, "Observers may not resign.");
            return;
        }
        game.gameState().resign(username, playerColor);
        new SQLGameDAO().updateGame(game);

        // send message of resignation
        String msgStr = username + " has resigned from the game as the " + playerColor.toString() + " player.";
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, msgStr, null, playerColor);
        connections.broadcast(game.gameID(), null, message);
    }

    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        connections.add(command, session);
        connections.printGame(new UserGameCommand(UserGameCommand.CommandType.CONNECT, command.getAuthToken(), command.getGameID()));
    }
    private void disconnect(UserGameCommand command) throws IOException, DataAccessException {
        connections.remove(command);
        var username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        var game = new SQLGameDAO().getGame(command.getGameID());
        if (game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            new SQLGameDAO().updateGame(new Game(game.gameID(), game.gameName(), null, game.blackUsername(), game.gameState()));
        }
        if (game.blackUsername() != null && game.blackUsername().equals(username)) {
            new SQLGameDAO().updateGame(new Game(game.gameID(), game.gameName(), game.whiteUsername(), null, game.gameState()));
        }
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game.", null, null);
        connections.broadcast(command.getGameID(), username, message);
    }

    private void makeMove(UserGameCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException {
        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        if (username == null) {
            sendErrorMessage(session, "You are not logged in. Please log in first.");
        }
        // get board
        var gameDAO = new SQLGameDAO();
        Game game = gameDAO.getGame(command.getGameID());
        // check if observer is trying to make a move
        if (!game.whiteUsername().equals(username) && !game.blackUsername().equals(username)) {
            command.isPlayer = false;
            sendErrorMessage(session, "Observers may not make moves.");
            return;
        }
        // get playerColor
        ChessGame.TeamColor playerColor = null;
        if (game.whiteUsername() != null && game.whiteUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (game.blackUsername() != null && game.blackUsername().equals(username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        // check to see if it's the turn of the person trying to make a move
        if (game.gameState().getTeamTurn() != playerColor) {
            sendErrorMessage(session, "It's not your turn yet!");
            return;
        }

        // check if a player is trying to make a move for the other team
        if (game.gameState().getBoard().getPiece(command.move.getStartPosition()).getTeamColor() != playerColor) {
            sendErrorMessage(session, "You may only move your own pieces.");
            return;
        }


        // attempt to make the move
        if (game.gameState().getGameOver()) {
            sendErrorMessage(session, "You may not make a move because the game has already ended.");
            return;
        }
        // check if promotion
        boolean isPromotion = game.gameState().isPromotion(command.move);
        if (isPromotion && command.move.getPromotionPiece() == null) {
            sendErrorMessage(session, "This piece will be promoted if you make this move; refer to the help menu," +
                    "then correctly enter in the parameters again, including the piece you want to promote to.");
        } else if (!isPromotion && command.move.getPromotionPiece() != null) {
            sendErrorMessage(session, "This piece cannot be promoted at this time. Please try making another move.");;
        }
        game.gameState().makeMove(command.move);

        // update game
        gameDAO.updateGame(game);
        // notify players
        String notification = String.format("%s moved their %s from %s to %s",
                username, game.gameState().getBoard().getPiece(command.move.getEndPosition()).getPieceType().toString(), command.move.getStartPosition(), command.move.getEndPosition());
        // print updated game state
//        connections.broadcast(game.gameID(), null, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game, null));
        connections.printGame(command);

        // notify users
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification, null, null);
        connections.broadcast(game.gameID(), username, message);

    }

    private void sendErrorMessage(Session session, String errorMessage) throws IOException {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage,  null, null);
        session.getRemote().sendString(message.toString());
    }

}
