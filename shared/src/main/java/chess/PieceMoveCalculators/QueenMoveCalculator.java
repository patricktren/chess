package chess.PieceMoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.List;

public class QueenMoveCalculator extends PieceMovesCalculator {
    public QueenMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validPositions = new ArrayList<>();

        // up
        int r = myPosition.getRow() + 1, c = myPosition.getColumn();
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r += 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // down
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn();
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r -= 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // right
        r = myPosition.getRow();
        c = myPosition.getColumn() + 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            c += 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // left
        r = myPosition.getRow();
        c = myPosition.getColumn() - 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            c -= 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // up-right
        r = myPosition.getRow() + 1;
        c = myPosition.getColumn() + 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r += 1;
            c += 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // up-left
        r = myPosition.getRow() + 1;
        c = myPosition.getColumn() - 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r += 1;
            c -= 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // down-right
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn() + 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r -= 1;
            c += 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        // down-left
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn() - 1;
        while (inBoardRange(r, c)) {
            var thisPosition = new ChessPosition(r, c);
            var piece = board.getPiece(thisPosition);
            r -= 1;
            c -= 1;
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
                break;
            } else break;
        }

        return validPositions;
    }
}
