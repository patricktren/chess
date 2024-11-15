package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;



    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public void drawChessBoard(ChessBoard board, ChessGame.TeamColor teamColor) {
        drawHeaderFooter();
        Boolean startsLight = true;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            for (int i = 7; i >= 0; i--) {
                drawMargin(i + 1);
                drawRow(board.getSquares()[i], startsLight);
                startsLight = !startsLight;
                drawMargin(i + 1);
                out.println();
            }
        } else {
            for (int i = 0; i <= 7; i++) {
                drawMargin(7 + 1 - i);
                drawRow(board.getSquares()[i], startsLight);
                startsLight = !startsLight;
                drawMargin(7 + 1 - i);
                out.println();
            }
        }
        drawHeaderFooter();
    }

    private void drawHeaderFooter() {
        String letterSpacing = "   ";
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("      ");
        out.print("a" + letterSpacing + "b" + letterSpacing + "c" + "  " + "d" + letterSpacing +
                "e" + "  " + "f" + letterSpacing + "g" + letterSpacing + "h" + letterSpacing + letterSpacing);
        // revert color and print line.
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private void drawMargin(Integer number) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("  " + number + "  ");

        // revert color
        out.print(RESET_TEXT_COLOR);
    }

    private void drawRow(ChessPiece[] row, Boolean startsLight) {
        boolean isLight = startsLight;
        for (int i=0; i < row.length; i++) {
            if (isLight) {
                out.print(SET_BG_COLOR_LIGHT_BEIGE);
            } else {
                out.print(SET_BG_COLOR_DARK_BEIGE);
            }
            ChessPiece piece = row[i];
            String toPrint = "";
            if (piece == null) {
                toPrint = EMPTY;
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    toPrint = BLACK_PAWN;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    toPrint = BLACK_KING;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    toPrint = BLACK_QUEEN;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    toPrint = BLACK_ROOK;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    toPrint = BLACK_KNIGHT;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    toPrint = BLACK_BISHOP;
                }
            } else {
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    toPrint = WHITE_PAWN;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    toPrint = WHITE_KING;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    toPrint = WHITE_QUEEN;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    toPrint = WHITE_ROOK;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    toPrint = WHITE_KNIGHT;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    toPrint = WHITE_BISHOP;
                }
            }

            out.print(toPrint);
            isLight = !isLight;
        }
        out.print(RESET_BG_COLOR);
    }
}