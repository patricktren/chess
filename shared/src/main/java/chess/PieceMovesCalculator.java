package chess;

import chess.PieceMoveCalculators.*;

import java.util.List;

public class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition myPosition;
    public final int LOWERBOUND = 1;
    public final int UPPERBOUND = 8;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }
    protected boolean inBoardRange(int row, int col) {
        return (row >= LOWERBOUND) && (col >= LOWERBOUND) && (row <= UPPERBOUND) && (col <= UPPERBOUND);
    }
    public List<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            return new BishopMoveCalculator(board, myPosition).calculateMoves(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            return new RookMoveCalculator(board, myPosition).calculateMoves(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
            return new QueenMoveCalculator(board, myPosition).calculateMoves(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return new KnightMoveCalculator(board, myPosition).calculateMoves(board, myPosition);
        } else if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            return new PawnMovesCalculator(board, myPosition).calculateMoves(board, myPosition);
        } else return null;
    }

}
