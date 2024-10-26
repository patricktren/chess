package chess.piecemovecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMoveCalculator;

import java.util.ArrayList;
import java.util.List;

public class KingMoveCalculator extends PieceMoveCalculator {

    public KingMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // check all king moves
        // up one
        var targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        var targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // down one
        targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // right one
        targetPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // left one
        targetPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // up one right one
        targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // up one left one
        targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // down one left one
        targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }
        // down one right one
        targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        targetMove = checkSquareBasic(board, myPosition, targetPosition);
        if (targetMove != null) {
            validMoves.add(targetMove);
        }


        return validMoves;
    }
}
