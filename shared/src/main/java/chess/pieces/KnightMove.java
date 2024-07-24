package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

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

        positions.put(Location.FL, new ChessPosition(mRow + 2, mCol - 1));
        positions.put(Location.LF, new ChessPosition(mRow + 1, mCol - 2));
        positions.put(Location.FR, new ChessPosition(mRow + 2, mCol + 1));
        positions.put(Location.RF, new ChessPosition(mRow + 1, mCol + 2));
        positions.put(Location.BL, new ChessPosition(mRow - 2, mCol - 1));
        positions.put(Location.LB, new ChessPosition(mRow - 1, mCol - 2));
        positions.put(Location.BR, new ChessPosition(mRow - 2, mCol + 1));
        positions.put(Location.RB, new ChessPosition(mRow - 1, mCol + 2));

        List<Location> locations = Arrays.asList(Location.LF, Location.RF, Location.LB, Location.RB,
                Location.FL, Location.FR, Location.BL, Location.BR);

        for (Location loc : locations) {

            if (!validate(positions.get(loc))) {
                cont.put(loc, false);
            } else if (cont.get(loc)) {
                moves.add(new ChessMove(mPosition, positions.get(loc), null));
                if (mBoard.getPiece(positions.get(loc)) != null) {
                    cont.put(loc, false);
                }
            }
        }
        return moves;
    }
}