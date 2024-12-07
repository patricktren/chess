package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class TestBoardDrawer {
    public static void main(String[] args) {
        BoardDrawer draw = new BoardDrawer();
        var board = new ChessBoard();
        board.resetBoard();
        draw.drawChessBoard(board, ChessGame.TeamColor.BLACK, null);
    }
}
