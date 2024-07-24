package chess;


import chess.pieces.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor mColor;
    private ChessPiece.PieceType mType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        mColor = pieceColor;
        mType = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return mColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return mType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();

        MoveCalculator move;

        switch (board.getPiece(myPosition).getPieceType()){
            case KING -> move = new KingMove(board, myPosition);
            case PAWN -> move = new PawnMove(board, myPosition);
            case ROOK -> move = new RookMove(board, myPosition);
            case BISHOP -> move = new BishopMove(board, myPosition);
            case KNIGHT -> move = new KnightMove(board, myPosition);
            case QUEEN -> move = new QueenMove(board, myPosition);
            case null, default -> throw new Error("invalid piece type");
        }

        return move.moves();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return mColor == that.mColor && mType == that.mType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mColor, mType);
    }

    @Override
    public String toString() {
        String piece = "null";
        switch (mType){
            case QUEEN -> piece = "|"+ "Q" + "|";
            case ROOK -> piece ="|"+ "R" + "|";
            case PAWN -> piece ="|"+ "P" + "|";
            case KING -> piece ="|"+ "K" + "|";
            case BISHOP -> piece ="|"+ "B" + "|";
            case KNIGHT -> piece ="|"+ "N" + "|";
        }
        if (mColor == ChessGame.TeamColor.BLACK){
            piece = piece.toLowerCase();
        }
        return  piece;
    }
}
