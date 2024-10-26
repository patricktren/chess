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
