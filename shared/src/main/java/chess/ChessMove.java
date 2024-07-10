package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition m_start;
    private ChessPosition m_end;
    private ChessPiece.PieceType m_promP;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        m_start = startPosition;
        m_end = endPosition;
        m_promP = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return m_start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return m_end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return m_promP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(m_start, chessMove.m_start) && Objects.equals(m_end, chessMove.m_end) && m_promP == chessMove.m_promP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_start, m_end, m_promP);
    }

    @Override
    public String toString() {
        return "from " + m_start +
                " to " + m_end +
                ", " + m_promP + "\n";
    }
}
