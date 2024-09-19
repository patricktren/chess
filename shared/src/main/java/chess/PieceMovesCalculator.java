package chess;

import chess.PieceMoveCalculators.BishopMoveCalculator;

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
    protected boolean InBoardRange(int row, int col) {
        return (row >= LOWERBOUND) && (col >= LOWERBOUND) && (row <= UPPERBOUND) && (col <= UPPERBOUND);
    }
    public List<ChessMove> CalculateMoves(ChessBoard board, ChessPosition myPosition){
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            return new BishopMoveCalculator(board, myPosition).CalculateMoves(board, myPosition);
        }
        else return null;
    }


}
