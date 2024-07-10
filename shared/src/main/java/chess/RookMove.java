package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RookMove extends MoveCalculator{

    public RookMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        for (int i = 1; i < 9; i++){

            positions.put(Location.F, new ChessPosition(m_row + i, m_col));
            positions.put(Location.L, new ChessPosition(m_row, m_col - i));
            positions.put(Location.R, new ChessPosition(m_row, m_col + i));
            positions.put(Location.B, new ChessPosition(m_row - i, m_col));

            List<Location> locations = Arrays.asList(Location.F, Location.L, Location.R, Location.B);

            for (Location loc : locations){

                if (!validate(positions.get(loc))){
                    cont.put(loc, false);
                } else if (cont.get(loc)){
                    moves.add(new ChessMove(m_position, positions.get(loc), null));
                    if (m_board.getPiece(positions.get(loc)) != null){
                        cont.put(loc, false);
                    }
                }

            }
        }

        return moves;
    }
}