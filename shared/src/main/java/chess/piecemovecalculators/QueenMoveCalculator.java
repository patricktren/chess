package chess.piecemovecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMoveCalculator;

import java.util.ArrayList;
import java.util.List;

public class QueenMoveCalculator extends PieceMoveCalculator {
    public QueenMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // up
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.UP, null));

        // down
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.DOWN, null));

        // right
        validMoves.addAll(getMoves(board, myPosition, null, MoveDirection.RIGHT));

        // left
        validMoves.addAll(getMoves(board, myPosition, null, MoveDirection.LEFT));

        // up right
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.UP, MoveDirection.RIGHT));

        // up left
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.UP, MoveDirection.LEFT));

        // down left
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.DOWN, MoveDirection.LEFT));

        // down right
        validMoves.addAll(getMoves(board, myPosition, MoveDirection.DOWN, MoveDirection.RIGHT));

        return validMoves;
    }
}
