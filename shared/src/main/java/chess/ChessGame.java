package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessBoard m_board = new ChessBoard();
    public ChessGame.TeamColor m_teamTurn;


    public ChessGame() {
        m_teamTurn = TeamColor.WHITE;
        m_board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return m_teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        m_teamTurn = team;
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    private boolean checkForCheck(ChessPosition position, TeamColor teamColor){
        boolean check = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (ChessMove move : piece.pieceMoves(m_board, currPos)) {
                        if (position == move.getEndPosition()) {
                            check = true;
                        }
                    }
                }
            }
        }
        return check;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);
                if (piece != null && piece.getTeamColor() != teamColor){
                    for (ChessMove move : piece.pieceMoves(m_board, currPos)){
                        ChessPiece target = m_board.getPiece(move.getEndPosition());
                        if (target != null && target.getPieceType() == ChessPiece.PieceType.KING){
                            check = true;
                        }
                    }
                }
            }
        }
        return check;
    }

    private ChessPosition findPiece(TeamColor teamColor, ChessPiece.PieceType pieceType){
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);
                if (piece != null && piece.getPieceType() == pieceType && piece.getTeamColor() == teamColor) {
                    return currPos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = false;

        if (isInCheck(teamColor)) {
            ChessPosition kingPos = findPiece(teamColor, ChessPiece.PieceType.KING);
            if (findPiece(teamColor, ChessPiece.PieceType.KING) != null) {
                ChessPiece king = m_board.getPiece(kingPos);
                Collection<ChessMove> moves = king.pieceMoves(m_board, kingPos);
                if (moves.isEmpty()) {
                    checkmate = true;
                } else {
                    boolean validMove = false;
                    for (ChessMove move : moves) {
                        m_board.addPiece(move.getEndPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        m_board.addPiece(move.getStartPosition(), null);
                        if (!isInCheck(teamColor)) {
                            validMove = true;
                        }
                        m_board.addPiece(move.getStartPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        m_board.addPiece(move.getEndPosition(), null);
                    }
                    if (!validMove) {
                        checkmate = true;
                    }
                }
            }
        }
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        m_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return m_board;
    }
}
