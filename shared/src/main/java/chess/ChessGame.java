package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeamTurn;
    private ChessBoard board;
    private boolean gameOver = false;
    private String winner = "";
    private TeamColor winnerColor;
    private boolean stalemate = false;

    public ChessGame() {
        board = new ChessBoard();
        currentTeamTurn = TeamColor.WHITE;
        resetBoard(board);
    }

    public void resign(String username, TeamColor playerColor) {
        winner = username;
        this.winnerColor = playerColor;
        gameOver = true;
    }

    public void stalemate() {
        stalemate = true;
        gameOver = true;
    }

    public boolean getGameOver() {
        return gameOver;
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
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
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
//        System.out.println(validMoves);
        return validMoves;

    }

    public boolean isPromotion(ChessMove move) {
        // return false if the piece isn't a pawn
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece != null || !piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
             return false;
        }
        // now we know it's a pawn; check to see if
        if (piece.getTeamColor().equals(TeamColor.BLACK) && move.getEndPosition().getRow() == 1) {
            return true;
        }
        if (piece.getTeamColor().equals(TeamColor.WHITE) && move.getEndPosition().getRow() == 8) {
            return true;
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startingPositionPiece = board.getPiece(move.startPosition);

        // check for invalid move
        if (startingPositionPiece == null) { // starting position is empty
            throw new InvalidMoveException("Incorrect syntax for moving; check the help menu.");
        }
        // is it the right player's turn?
        if (startingPositionPiece.getTeamColor() != currentTeamTurn) {
            throw new InvalidMoveException("It's not your turn.");
        }
        var validMoves = validMoves(move.startPosition);
        // move is valid?
        if (validMoves.contains(move)) {
            board.addPiece(move.startPosition, null);
            board.addPiece(move.endPosition, startingPositionPiece);
            // is promotion
            if (move.getPromotionPiece() != null) {
                board.addPiece(move.endPosition, new ChessPiece(startingPositionPiece.getTeamColor(), move.getPromotionPiece()));
            }
        }
        else {
            throw new InvalidMoveException("The move is invalid.");
        }
        if (startingPositionPiece.getTeamColor() == TeamColor.WHITE) {
            currentTeamTurn = TeamColor.BLACK;
        }
        else {
            currentTeamTurn = TeamColor.WHITE;
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
                // is kingPosition?
                if (piece != null
                        && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    kingPosition = position;
                }
                // is enemy piece?
                else if (piece != null
                        && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> piecePossibleMoves = board.getPiece(position).pieceMoves(board, position);
                    for (ChessMove move:piecePossibleMoves) {
                        enemyPossiblePositions.add(move.endPosition);
                    }
                }
            }
        }

        // check if kingPosition is in the set of enemyPossiblePositions
        return enemyPossiblePositions.contains(kingPosition);
    }

    public ArrayList<ChessMove> validMoves(TeamColor teamColor) {
        var validMoves = new ArrayList<ChessMove>();
        // try making all my moves and see if
        for (int i=1; i <= board.squares.length; i++) {
            for (int j=1; j <= board.squares[i - 1].length; j++) {
                var startPosition = new ChessPosition(i,j);
                var startPiece = board.getPiece(startPosition);
                if (startPiece != null && startPiece.getTeamColor() == teamColor) {
                    validMoves.addAll(validMoves(startPosition));
                }
            }
        }
        return validMoves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessMove> validMoves = validMoves(teamColor);
        if (board.toString() == "") {
            return false;
        }
        return (validMoves.isEmpty());
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ArrayList<ChessMove> validMoves = validMoves(teamColor);
        if (board.toString() == "") {
            return false;
        }
        return (validMoves.isEmpty() && !isInCheck(teamColor));
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public void resetBoard(ChessBoard board) {
        board.resetBoard();
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
