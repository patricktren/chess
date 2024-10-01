package chess;

import chess.PieceMoveCalculators.*;

import java.util.List;

public class PieceMoveCalculator {
    ChessBoard board;
    ChessPosition myPosition;

    public PieceMoveCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public List<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            return new BishopMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            return new RookMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
            return new QueenMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return new KnightMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING) {
            return new KingMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            return new PawnMoveCalculator(board, myPosition).moveCalculator(board, myPosition);
        }
        else {return null;}
    }

    protected boolean isInBounds(ChessPosition position) {
        return (position.getRow() <= board.UPPERBOUND && position.getRow() >= board.LOWERBOUND
            && position.getColumn() <= board.UPPERBOUND && position.getColumn() >= board.LOWERBOUND);
    }

    protected ChessMove checkSquareBasic(ChessBoard board, ChessPosition myPosition, ChessPosition targetPosition) {
        if (isInBounds(targetPosition) &&
                (board.getPiece(targetPosition) == null
                || board.getPiece(targetPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            return new ChessMove(myPosition, targetPosition, null);
        } else return null;
    }
}
