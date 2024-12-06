package websocket;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
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
import java.util.concurrent.ExecutionException;

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
    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        connections.add(command, session);
        connections.printGame(new UserGameCommand(UserGameCommand.CommandType.CONNECT, command.getAuthToken(), command.getGameID()));
    }
    private void disconnect(UserGameCommand command) throws IOException, DataAccessException {
        connections.remove(command);
    }

    private void makeMove(UserGameCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException {
        String username = new SQLAuthTokenDAO().getAuthToken(command.getAuthToken()).getUsername();
        if (username == null) {
            sendErrorMessage(session, "You are not logged in. Please log in first.");
        }

        String[] params = command.move.split(" ");
        if (params.length != 2 & params.length != 3) {

            sendErrorMessage(session, "Incorrect number of parameters entered; please refer to the help menu.");
        }
        // parse move command into ChessMove
        ChessMove move = null;
        try {
            Integer rowStart = Integer.parseInt(String.valueOf(params[0].charAt(1)));
            Integer colStart = parseLetterToInt(params[0].charAt(0));

            Integer rowEnd = Integer.parseInt(String.valueOf(params[1].charAt(1)));
            Integer colEnd = parseLetterToInt(params[1].charAt(0));

            ChessPiece.PieceType promotion = null;
            if (params.length == 3) {
                String promotionStr = params[2];
                promotion = ChessPiece.PieceType.valueOf(promotionStr.toUpperCase());
            }
            move = new ChessMove(new ChessPosition(rowStart, colStart), new ChessPosition(rowEnd, colEnd), promotion);
        }
        catch (Exception e) {
            sendErrorMessage(session, "Parameters were incorrectly entered; please refer to the help menu");
        }
        // attempt to make the move
        // get board
        var gameDAO = new SQLGameDAO();
        Game game = gameDAO.getGame(command.getGameID());
        // check if promotion
        boolean isPromotion = game.gameState().isPromotion(move);
        if (isPromotion && move.getPromotionPiece() == null) {
            sendErrorMessage(session, "This piece will be promoted if you make this move; refer to the help menu," +
                    "then correctly enter in the parameters again, including the piece you want to promote to.");
        } else if (!isPromotion && move.getPromotionPiece() != null) {
            sendErrorMessage(session, "This piece cannot be promoted at this time. Please try making another move.");;
        }
        game.gameState().makeMove(move);

        // update game
        gameDAO.updateGame(game);
        // notify players
        String notification = String.format("%s moved their %s from %s to %s",
                username, game.gameState().getBoard().getPiece(move.getEndPosition()).getPieceType().toString(), move.getStartPosition(), move.getEndPosition());
        // print updated game state
        connections.broadcast(game.gameID(), null, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game, null));

        // notify users
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification, null, null);
        connections.broadcast(game.gameID(), username, message);

    }

    private void sendErrorMessage(Session session, String errorMessage) throws IOException {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage,  null, null);
        session.getRemote().sendString(message.toString());
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
}
