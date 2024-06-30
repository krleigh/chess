package chess;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition m_startP;
    private ChessPosition m_endP;
    private ChessPiece.PieceType m_promotionP;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        m_startP = startPosition;
        m_endP = endPosition;
        m_promotionP = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
//        throw new RuntimeException("Not implemented");
        return m_startP;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
//        throw new RuntimeException("Not implemented");
        return m_endP;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
//        throw new RuntimeException("Not implemented");
        return m_promotionP;
    }
//
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessMove that = (ChessMove) o;
        boolean proP;
        if (this.m_promotionP == null || that.m_promotionP == null) {
           if (this.m_promotionP == null && that.m_promotionP == null){
               proP = true;
           } else{
               proP = false;
           }
        } else {
            proP  = this.m_promotionP.equals(that.m_promotionP);
        }
        return this.m_startP.equals(that.m_startP) && this.m_endP.equals(that.m_endP) && proP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_startP, m_endP, m_promotionP);
    }

    @Override
    public String toString() {
        return "from " + m_startP + " to " + m_endP;
    }
}
