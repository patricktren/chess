package chess.piecemovecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator extends PieceMoveCalculator {

    public PawnMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        List<ChessMove> validMovesPromotions = new ArrayList<>();

        // white piece
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // move forward one
            var targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (isInBounds(targetPosition) && board.getPiece(targetPosition) == null) {
                validMoves.add(new ChessMove(myPosition, targetPosition, null));
                // is at starting position and can move forward two
                targetPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (myPosition.getRow() == 2 && board.getPiece(targetPosition) == null) {
                    validMoves.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
            // attack up and right
            targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            var targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null && board.getPiece(targetPosition) != null
                    && board.getPiece(targetPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                validMoves.add(targetMove);
            }
            // attack up and left
            targetPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null && board.getPiece(targetPosition) != null
                    && board.getPiece(targetPosition).getTeamColor() != ChessGame.TeamColor.WHITE) {
                validMoves.add(targetMove);
            }
            // one of these results in promotion
            for (ChessMove validMove : validMoves) {
                if (validMove.getEndPosition().getRow() == 8) {
                    var startPosition = validMove.getStartPosition();
                    var endPosition = validMove.getEndPosition();

                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                } else {
                    validMovesPromotions.add(validMove);
                }
            }
        }
        // black piece
        else {
            // move forward one
            var targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (isInBounds(targetPosition) && board.getPiece(targetPosition) == null) {
                validMoves.add(new ChessMove(myPosition, targetPosition, null));
                // is at starting position and can move forward two
                targetPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (myPosition.getRow() == 7 && board.getPiece(targetPosition) == null) {
                    validMoves.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
            // attack up and right
            targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            var targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null && board.getPiece(targetPosition) != null
                    && board.getPiece(targetPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                validMoves.add(targetMove);
            }
            // attack up and left
            targetPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null && board.getPiece(targetPosition) != null
                    && board.getPiece(targetPosition).getTeamColor() != ChessGame.TeamColor.BLACK) {
                validMoves.add(targetMove);
            }
            // one of these results in promotion
            for (ChessMove validMove : validMoves) {
                if (validMove.getEndPosition().getRow() == 1) {
                    var startPosition = validMove.getStartPosition();
                    var endPosition = validMove.getEndPosition();

                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    validMovesPromotions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                } else {
                    validMovesPromotions.add(validMove);
                }
            }
        }

        return validMovesPromotions;
    }
}
