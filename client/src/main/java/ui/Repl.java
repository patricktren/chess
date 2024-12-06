package ui;

import chess.ChessGame;
import model.AuthToken;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    public void repl(Client client, String exitInput, Scanner scanner, String authToken) {
        String result = "";
        while (!result.equals(exitInput)) {
            String line = scanner.nextLine();
            try {
                result = client.evalInput(line, authToken);
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
    }
    public String helpPrompt() {
        return "";
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
            System.out.println(">>> " + notification.getMessage() + "\n");
        }
        else if (notification.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            if (notification.getPlayerColor() != null) {
                new BoardDrawer().drawChessBoard(notification.getGame().gameState().getBoard(), notification.getPlayerColor(), null);
            } else {
                new BoardDrawer().drawChessBoard(notification.getGame().gameState().getBoard(), ChessGame.TeamColor.WHITE, null);
            }
        }
    }
}
