package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int m_row;
    private final int m_col;

    public ChessPosition(int row, int col) {
        //final makes object immutable
        this.m_row = row-1;
        this.m_col = col-1;
        }


    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
//        throw new RuntimeException("Not implemented");
        return m_row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
//        throw new RuntimeException("Not implemented");
        return m_col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return m_row == that.m_row && m_col == that.m_col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_row, m_col);
    }
}
