package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class KnightMove extends MoveCalculator{

    public KnightMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        positions.put(Location.FL, new ChessPosition(m_row + 2, m_col - 1));
        positions.put(Location.LF, new ChessPosition(m_row + 1, m_col - 2));
        positions.put(Location.FR, new ChessPosition(m_row + 2, m_col + 1));
        positions.put(Location.RF, new ChessPosition(m_row + 1, m_col + 2));
        positions.put(Location.BL, new ChessPosition(m_row - 2, m_col - 1));
        positions.put(Location.LB, new ChessPosition(m_row - 1, m_col - 2));
        positions.put(Location.BR, new ChessPosition(m_row - 2, m_col + 1));
        positions.put(Location.RB, new ChessPosition(m_row - 1, m_col + 2));

        List<Location> locations = Arrays.asList(Location.LF, Location.RF, Location.LB, Location.RB,
                Location.FL, Location.FR, Location.BL, Location.BR);

        for (Location loc : locations) {

            if (!validate(positions.get(loc))) {
                cont.put(loc, false);
            } else if (cont.get(loc)) {
                moves.add(new ChessMove(m_position, positions.get(loc), null));
                if (m_board.getPiece(positions.get(loc)) != null) {
                    cont.put(loc, false);
                }
            }
        }
        return moves;
    }
}