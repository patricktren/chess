package chess.PieceMoveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator extends PieceMovesCalculator {
    public PawnMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    @Override
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validPositions = new ArrayList<>();

        // is white pawn:
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {

            // can move forward one
            var positionOneForward = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (inBoardRange(positionOneForward.getRow(), positionOneForward.getColumn())
                && board.getPiece(positionOneForward) == null) {
                validPositions.add(new ChessMove(myPosition, positionOneForward, null));
            }
            // enemy up and right
            var positionEnemyUpRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (inBoardRange(positionEnemyUpRight.getRow(), positionEnemyUpRight.getColumn())
                && board.getPiece(positionEnemyUpRight) != null
                && board.getPiece(positionEnemyUpRight).getTeamColor() != board.getPiece(myPosition).getTeamColor() ) {
                validPositions.add(new ChessMove(myPosition, positionEnemyUpRight, null));
            }
            // enemy up and left
            var positionEnemyUpLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (inBoardRange(positionEnemyUpLeft.getRow(), positionEnemyUpLeft.getColumn())
                    && board.getPiece(positionEnemyUpLeft) != null
                    && board.getPiece(positionEnemyUpLeft).getTeamColor() != board.getPiece(myPosition).getTeamColor() ) {
                validPositions.add(new ChessMove(myPosition, positionEnemyUpLeft, null));
            }

            // is on starting row
            if (myPosition.getRow() == 2) {
                var positionOneForwardFromStarting = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                var positionTwoForward = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                // two positions ahead aren't blocked
                if (board.getPiece(positionTwoForward) == null
                    && board.getPiece(positionOneForwardFromStarting) == null) {
                    validPositions.add(new ChessMove(myPosition, positionTwoForward, null));
                }
            }
        }
        // is black pawn:
        else {
            // can move forward one
            var positionOneForward = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (inBoardRange(positionOneForward.getRow(), positionOneForward.getColumn())
                && board.getPiece(positionOneForward) == null) {
                validPositions.add(new ChessMove(myPosition, positionOneForward, null));
            }
            // enemy down and right
            var positionEnemyUpRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (inBoardRange(positionEnemyUpRight.getRow(), positionEnemyUpRight.getColumn())
                    && board.getPiece(positionEnemyUpRight) != null
                    && board.getPiece(positionEnemyUpRight).getTeamColor() != board.getPiece(myPosition).getTeamColor() ) {
                validPositions.add(new ChessMove(myPosition, positionEnemyUpRight, null));
            }
            // enemy up and left
            var positionEnemyUpLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (inBoardRange(positionEnemyUpLeft.getRow(), positionEnemyUpLeft.getColumn())
                    && board.getPiece(positionEnemyUpLeft) != null
                    && board.getPiece(positionEnemyUpLeft).getTeamColor() != board.getPiece(myPosition).getTeamColor() ) {
                validPositions.add(new ChessMove(myPosition, positionEnemyUpLeft, null));
            }

            // is on starting row
            if (myPosition.getRow() == 7) {
                var positionOneForwardFromStarting = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                var positionTwoForward = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                // two positions ahead aren't blocked
                if (board.getPiece(positionTwoForward) == null
                        && board.getPiece(positionOneForwardFromStarting) == null) {
                    validPositions.add(new ChessMove(myPosition, positionTwoForward, null));
                }
            }
        }
        // handle promotions
        List<ChessMove> promotionAdditions = new ArrayList<>();
        for (ChessMove move : validPositions) {
            if (move.getEndPosition().getRow() == 8
                || move.getEndPosition().getRow() == 1) {
                promotionAdditions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                promotionAdditions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                promotionAdditions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
                promotionAdditions.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
            } else {
                promotionAdditions.add(move);
            }
        }
        validPositions = promotionAdditions;

        return validPositions;
    }

}
