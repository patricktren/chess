package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor currentTeamTurn;
    ChessBoard board;
    public ChessGame() {
        board = new ChessBoard();
        currentTeamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return currentTeamTurn == chessGame.currentTeamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeamTurn, board);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = new ChessPiece(board.getPiece(startPosition).getTeamColor(), board.getPiece(startPosition).getPieceType());
        var initialValidMoves = piece.pieceMoves(board, startPosition);
        var validMoves = new ArrayList<ChessMove>();

        ChessPiece endPositionOriginalPiece = null;

        // iterate over initialValidMoves
        for (ChessMove move:initialValidMoves) {
            // get endPositionOriginalPiece
            endPositionOriginalPiece = board.getPiece(move.endPosition);

            // make the move
            board.addPiece(move.startPosition, null);
            board.addPiece(move.endPosition, piece);
            if (isInCheck(piece.getTeamColor())) {
                board.addPiece(move.startPosition, piece);
                board.addPiece(move.endPosition, endPositionOriginalPiece);
                continue;
            }
            else {
                board.addPiece(move.startPosition, piece);
                board.addPiece(move.endPosition, endPositionOriginalPiece);
                validMoves.add(move);
            }
        }
        System.out.println(validMoves);
        return validMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // starting position is empty
        if (board.getPiece(move.startPosition) == null) {
            throw new InvalidMoveException();
        }
        var validMoves = validMoves(move.startPosition);
        // move is valid?
        if (validMoves.contains(move)) {
            board.addPiece(move.startPosition, null);
            board.addPiece(move.endPosition, board.getPiece(move.startPosition));
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean inCheck = false;
        ChessPosition kingPosition = null;
        var enemyPossiblePositions = new HashSet<ChessPosition>();

        for (int i=1; i <= board.squares.length; i++) {
            for (int j=1; j <= board.squares[i - 1].length; j++) {
                var position = new ChessPosition(i, j);
                var piece = board.getPiece(position);
                // piece isn't null
                if (piece != null) {
                    // is kingPosition?
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                        kingPosition = position;
                    }
                    // is enemy piece?
                    else if (piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> piecePossibleMoves = board.getPiece(position).pieceMoves(board, position);
                        for (ChessMove move:piecePossibleMoves) {
                            enemyPossiblePositions.add(move.endPosition);
                        }
                    }
                }
            }
        }

        // check if kingPosition is in the set of enemyPossiblePositions
        return enemyPossiblePositions.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board.squares = board.squares;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
