package chess.piecemovecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMoveCalculator;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveCalculator extends PieceMoveCalculator {

    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // check all knight moves
        // up two right one
        var targetPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        var targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // up two left one
        targetPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // down two right one
        targetPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // down two left one
        targetPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // right two up one
        targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // right two down one
        targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // left two up one
        targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // left two down one
        targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }

        return validMoves;
    }
}
