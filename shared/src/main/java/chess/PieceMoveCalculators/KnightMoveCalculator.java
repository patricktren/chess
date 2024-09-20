package chess.PieceMoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveCalculator extends PieceMovesCalculator {
    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validPositions = new ArrayList<>();

        // check positions

        // up two, right one
        if (inBoardRange(myPosition.getRow() + 2, myPosition.getColumn() + 1)) {
            var thisPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // up two, left one
        if (inBoardRange(myPosition.getRow() + 2, myPosition.getColumn() - 1)) {
            var thisPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // down two, left one
        if (inBoardRange(myPosition.getRow() - 2, myPosition.getColumn() - 1)) {
            var thisPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // down two, right one
        if (inBoardRange(myPosition.getRow() - 2, myPosition.getColumn() + 1)) {
            var thisPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // right two, up one
        if (inBoardRange(myPosition.getRow() + 1, myPosition.getColumn() + 2)) {
            var thisPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // right two, down one
        if (inBoardRange(myPosition.getRow() - 1, myPosition.getColumn() + 2)) {
            var thisPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // left two, up one
        if (inBoardRange(myPosition.getRow() + 1, myPosition.getColumn() - 2)) {
            var thisPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }

        // left two, down one
        if (inBoardRange(myPosition.getRow() - 1, myPosition.getColumn() - 2)) {
            var thisPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
            var piece = board.getPiece(thisPosition);
            if (piece == null) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            } else if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, thisPosition, null));
            }
        }


        return validPositions;
    }

}
