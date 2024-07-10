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
    private boolean m_madeMove;


    public ChessGame() {
        m_teamTurn = TeamColor.WHITE;
        m_board.resetBoard();
        m_madeMove = false;
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
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
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
            m_board.addPiece(move.getStartPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
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

    private Collection<ChessPosition> findChecker(TeamColor teamColor) {
        ArrayList<ChessPosition> checkers = new ArrayList<ChessPosition>();
        boolean checked =false;

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);

                if (piece != null && piece.getTeamColor() != teamColor){



                    for (ChessMove move : piece.pieceMoves(m_board, currPos)){
                        ChessPiece target = m_board.getPiece(move.getEndPosition());
                        if (target != null && target.getPieceType() == ChessPiece.PieceType.KING && checked==false){
                            checkers.add(currPos);
                            checked = true;

                        }
                    }
                }
            }
        }

        return checkers;
    }

    private boolean canCapture(TeamColor teamColor, ChessPosition checkerPos){
        boolean canCap = false;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece piece = m_board.getPiece(currPos);
                if (piece != null && piece.getTeamColor() == teamColor){

                    Collection<ChessMove> moves = piece.pieceMoves(m_board, currPos);


                    for (ChessMove move : moves){
                        System.out.println("end" + move.getEndPosition());
                        System.out.println("checker" + checkerPos);
                        if (move.getEndPosition() == checkerPos){
                            canCap=true;
                            System.out.println(piece);
                        }
                    }
                }
            }
        }
        return canCap;
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
            Collection<ChessPosition> checkerPos = findChecker(teamColor);
            System.out.println(checkerPos);
            if (findPiece(teamColor, ChessPiece.PieceType.KING) != null) {
                ChessPiece king = m_board.getPiece(kingPos);
                Collection<ChessMove> moves = king.pieceMoves(m_board, kingPos);
                if (moves.isEmpty()) {
                    checkmate = true;
                } else {
                    boolean validMove = false;

                    for (ChessMove move : moves) {
                        ChessPiece capturePiece = m_board.getPiece(move.getEndPosition());
                        if (capturePiece !=null ){


                        }
                        m_board.addPiece(move.getEndPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        m_board.addPiece(move.getStartPosition(), null);
                        if (!isInCheck(teamColor)) {
                            validMove = true;

                        }
                        m_board.addPiece(move.getStartPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
                        m_board.addPiece(move.getEndPosition(), capturePiece);
                    }
                    if (!validMove) {
                        checkmate = true;
                    }
                }

                for (ChessPosition checker : checkerPos){
                    if (canCapture(teamColor, checker)){
                        checkmate = false;
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
