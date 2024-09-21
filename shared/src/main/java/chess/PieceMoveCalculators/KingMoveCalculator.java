package chess.PieceMoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.List;

public class KingMoveCalculator extends PieceMovesCalculator {
    public KingMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    public boolean isValidSquare(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition) {
        return inBoardRange(newPosition.getRow(), newPosition.getColumn())
                && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor());
    }

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validPositions = new ArrayList<>();

        // king moves
        var positionUp = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (isValidSquare(board, myPosition, positionUp)) {
            validPositions.add(new ChessMove(myPosition, positionUp, null));
        }

        var positionDown = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (isValidSquare(board, myPosition, positionDown)) {
            validPositions.add(new ChessMove(myPosition, positionDown, null));
        }

        var positionRight = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        if (isValidSquare(board, myPosition, positionRight)) {
            validPositions.add(new ChessMove(myPosition, positionRight, null));
        }

        var positionLeft = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        if (isValidSquare(board, myPosition, positionLeft)) {
            validPositions.add(new ChessMove(myPosition, positionLeft, null));
        }

        var positionUpRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (isValidSquare(board, myPosition, positionUpRight)) {
            validPositions.add(new ChessMove(myPosition, positionUpRight, null));
        }

        var positionUpLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (isValidSquare(board, myPosition, positionUpLeft)) {
            validPositions.add(new ChessMove(myPosition, positionUpLeft, null));
        }

        var positionDownRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (isValidSquare(board, myPosition, positionDownRight)) {
            validPositions.add(new ChessMove(myPosition, positionDownRight, null));
        }

        var positionDownLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (isValidSquare(board, myPosition, positionDownLeft)) {
            validPositions.add(new ChessMove(myPosition, positionDownLeft, null));
        }

        return validPositions;
    }
}
