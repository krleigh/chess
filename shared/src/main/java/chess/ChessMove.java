package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition mStart;
    private final ChessPosition mEnd;
    private final ChessPiece.PieceType mPromP;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        mStart = startPosition;
        mEnd = endPosition;
        mPromP = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return mStart;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return mEnd;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return mPromP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(mStart, chessMove.mStart) && Objects.equals(mEnd, chessMove.mEnd) && mPromP == chessMove.mPromP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mStart, mEnd, mPromP);
    }

    @Override
    public String toString() {
        return "from " + mStart +
                " to " + mEnd +
                ", " + mPromP + "\n";
    }
}
