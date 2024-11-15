package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class TestBoardDrawer {
    public static void main(String[] args) {
        BoardDrawer draw = new BoardDrawer();
        draw.drawChessBoard(new ChessBoard(), ChessGame.TeamColor.WHITE);
    }
}
