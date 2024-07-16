package chess;

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
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = m_board.getPiece(startPosition);
        TeamColor teamColor = piece.getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(m_board, startPosition);
        for(ChessMove move : moves){
            ChessPiece capturePiece = m_board.getPiece(move.getEndPosition());

            m_board.addPiece(move.getEndPosition(), piece);
            m_board.addPiece(move.getStartPosition(), null);
            if (!isInCheck(teamColor)) {
                validMoves.add(move);

            }
            m_board.addPiece(move.getStartPosition(), piece);
            m_board.addPiece(move.getEndPosition(), capturePiece);

        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = m_board.getPiece(move.getStartPosition());
        if (piece != null ){
            Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
            TeamColor teamColor = piece.getTeamColor();
            if (teamColor == m_teamTurn && validMoves.contains(move) ){
                ChessPiece.PieceType promP = move.getPromotionPiece();
                if(promP != null) {
                    piece = new ChessPiece (teamColor, promP);
                }
                m_board.addPiece(move.getEndPosition(), piece);
                m_board.addPiece(move.getStartPosition(), null);
                if (m_teamTurn == TeamColor.WHITE){
                    m_teamTurn = TeamColor.BLACK;
                } else {
                    m_teamTurn = TeamColor.WHITE;
                }

            } else {
                throw new InvalidMoveException();
            }
        } else {
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

    private Collection<ChessPosition> findPieces(TeamColor teamColor){
        ArrayList<ChessPosition> pieces = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    pieces.add(currPos);
                }
            }
        }
        return pieces;
    }

          /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = false;

        if(isInCheck(teamColor)){
            ArrayList<ChessMove> moves = new ArrayList<>();
            Collection<ChessPosition> pieces = findPieces(teamColor);
            for (ChessPosition piecePos : pieces){
                moves.addAll(validMoves(piecePos));
            }
            if (moves.isEmpty()){
                checkmate = true;
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
        boolean stalemate = false;
        ArrayList<ChessMove> moves = new ArrayList<>();
        Collection<ChessPosition> pieces = findPieces(teamColor);
        for (ChessPosition piecePos : pieces){
            moves.addAll(validMoves(piecePos));
        }
        if (moves.isEmpty() && !isInCheck(teamColor)){
            stalemate = true;
        }
        return stalemate;
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
