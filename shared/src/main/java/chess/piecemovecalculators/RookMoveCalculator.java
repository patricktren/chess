package chess.piecemovecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMoveCalculator;

import java.util.ArrayList;
import java.util.List;

public class RookMoveCalculator extends PieceMoveCalculator {
    public RookMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // up
        int r = myPosition.getRow() + 1;
        int c = myPosition.getColumn();
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            r += 1; // increment row
        }

        // down
        r = myPosition.getRow() - 1;
        c = myPosition.getColumn();
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            r -= 1; // increment row
        }

        // right
        r = myPosition.getRow();
        c = myPosition.getColumn() + 1;
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            c += 1; // increment col
        }

        // right
        r = myPosition.getRow();
        c = myPosition.getColumn() - 1;
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            c -= 1; // increment col
        }

        return validMoves;
    }
}
