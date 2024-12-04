package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;



    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public void drawChessBoard(ChessBoard board, ChessGame.TeamColor playerColor, ChessPosition highlightPiecePosition) {
        ArrayList<ChessMove> toHighlightMoves = null;
        ArrayList<ChessPosition> positionsToHighlight = new ArrayList<>();
        // highlight positions
        if (highlightPiecePosition != null && board.getPiece(highlightPiecePosition) != null) {
            // get possible moves and convert to ChessPositions
            toHighlightMoves = new ArrayList<>(board.getPiece(highlightPiecePosition).pieceMoves(board, highlightPiecePosition));
            positionsToHighlight = new ArrayList<>();
            for (ChessMove move:toHighlightMoves) {
                positionsToHighlight.add(move.getEndPosition());
            }
        }


        drawHeaderFooter(playerColor);
        boolean startsLight = true;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            for (int r = 7; r >= 0; r--) {
                drawMargin(7 + 1 - r);
                ChessPiece[] row = board.getSquares()[r];
                List<ChessPiece> rowList = Arrays.asList(row).reversed();
                row = rowList.toArray(row);
                drawRow(r, row, startsLight, positionsToHighlight, highlightPiecePosition, playerColor);
                startsLight = !startsLight;
                drawMargin(7 + 1 - r);
                out.println();
            }
        } else {
            for (int r = 0; r <= 7; r++) {
                drawMargin(7 + 1 - r);
                drawRow(r, board.getSquares()[r], startsLight, positionsToHighlight, highlightPiecePosition, playerColor);
                startsLight = !startsLight;
                drawMargin(7 + 1 - r);
                out.println();
            }
        }
        drawHeaderFooter(playerColor);
    }

    private void drawHeaderFooter(ChessGame.TeamColor playerColor) {
        String letterSpacing = "   ";
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("      ");
        String toPrint = "a" + letterSpacing + "b" + letterSpacing + "c" + "  " + "d" + letterSpacing +
                "e" + "  " + "f" + letterSpacing + "g" + letterSpacing + "h" + letterSpacing + EMPTY;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            toPrint = "h" + letterSpacing + "g" + letterSpacing + "f" + "  " + "e" + letterSpacing +
                    "d" + "  " + "c" + letterSpacing + "b" + letterSpacing + "a" + letterSpacing + EMPTY;
        }
        out.print(toPrint);
        // revert color and print line.
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawMargin(Integer number) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print("  " + number + "  ");

        // revert color
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
    }

    private void drawRow(Integer rowNum, ChessPiece[] row, Boolean startsLight, ArrayList<ChessPosition> toHighlight, ChessPosition piecePosition, ChessGame.TeamColor playerColor) {
        boolean isLight = startsLight;
        // highlight potential moves for white player
        for (int c = 0; c < row.length; c++) {
            if (playerColor == ChessGame.TeamColor.WHITE &&
                    toHighlight.contains(new ChessPosition(8 - (rowNum), c + 1))) {
                if (isLight) {
                    out.print(SET_BG_COLOR_LIGHT_GREEN_HIGHLIGHT);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
            }
            // highlight potential moves for black player
            else if (playerColor == ChessGame.TeamColor.BLACK &&
                    toHighlight.contains(new ChessPosition(8 - rowNum, 8 - c))) {
                if (isLight) {
                    out.print(SET_BG_COLOR_LIGHT_GREEN_HIGHLIGHT);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
            }
            // highlight starting position for white
            else if (playerColor == ChessGame.TeamColor.WHITE &&
                    piecePosition != null &&
                    piecePosition.equals(new ChessPosition(8 - rowNum, c + 1))) {
                out.print(SET_BG_COLOR_BLUE_HIGHLIGHT);
            }
            // highlight starting position for black
            else if (playerColor == ChessGame.TeamColor.BLACK &&
                    piecePosition != null &&
                    piecePosition.equals(new ChessPosition(8 - rowNum, 8 - c))) {
                out.print(SET_BG_COLOR_BLUE_HIGHLIGHT);
            }
            else if (isLight) {
                out.print(SET_BG_COLOR_LIGHT_BEIGE);
            } else {
                out.print(SET_BG_COLOR_DARK_BEIGE);
            }
            ChessPiece piece = row[c];
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
        out.print(RESET_TEXT_COLOR);
    }
}