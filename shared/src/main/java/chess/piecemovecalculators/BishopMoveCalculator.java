package chess.piecemovecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class BishopMoveCalculator extends PieceMoveCalculator {

    public BishopMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // up right
        int r = myPosition.getRow() + 1;
        int c = myPosition.getColumn() + 1;
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            r += 1;
            c += 1;
        }

        // up left
        r = myPosition.getRow() + 1;
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
            r += 1;
            c -= 1;
        }

        // down left
        r = myPosition.getRow() - 1;
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
            r -= 1;
            c -= 1;
        }

        // down right
        r = myPosition.getRow() - 1;
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
            r -= 1;
            c += 1;
        }

        return validMoves;
    }
}
