package chess;

import chess.piecemovecalculators.*;

import java.util.ArrayList;
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
        }
        else {return null;}
    }
    public enum MoveDirection {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }
    protected ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, MoveDirection yAxis, MoveDirection xAxis) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        // up/down
        if (yAxis == MoveDirection.UP) {
            r = myPosition.getRow() + 1;
        }
        else if (yAxis == MoveDirection.DOWN) {
            r = myPosition.getRow() - 1;
        }
        // left/right
        if (xAxis == MoveDirection.RIGHT) {
            c = myPosition.getColumn() + 1;
        }
        else if (xAxis == MoveDirection.LEFT) {
            c = myPosition.getColumn() - 1;
        }
        while (isInBounds(new ChessPosition(r, c))) {
            var targetPosition = new ChessPosition(r,c);
            ChessMove targetMove = checkSquareBasic(board, myPosition, targetPosition);
            if (targetMove != null) {
                validMoves.add(targetMove);
            }
            if (board.getPiece(targetPosition) != null) {
                break;
            }
            // up/down
            if (yAxis == MoveDirection.UP) {
                r += 1;
            }
            else if (yAxis == MoveDirection.DOWN) {
                r -= 1;
            }
            // left/right
            if (xAxis == MoveDirection.RIGHT) {
                c += 1;
            }
            else if (xAxis == MoveDirection.LEFT) {
                c -= 1;
            }
        }
        return validMoves;
    }
}
