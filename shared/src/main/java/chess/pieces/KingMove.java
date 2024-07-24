package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class KingMove extends MoveCalculator{

    public KingMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        for (int i = 1; i < 2; i++){

            positions.put(Location.F, new ChessPosition(mRow + i, mCol));
            positions.put(Location.L, new ChessPosition(mRow, mCol - i));
            positions.put(Location.R, new ChessPosition(mRow, mCol + i));
            positions.put(Location.B, new ChessPosition(mRow - i, mCol));

            positions.put(Location.FL, new ChessPosition(mRow + i, mCol - i));
            positions.put(Location.FR, new ChessPosition(mRow + i, mCol + i));
            positions.put(Location.BL, new ChessPosition(mRow - i, mCol -i));
            positions.put(Location.BR, new ChessPosition(mRow - i, mCol +i));

            List<Location> locations = Arrays.asList(Location.F, Location.L, Location.R, Location.B,
                    Location.FL, Location.FR, Location.BL, Location.BR);

            for (Location loc : locations){
                if (!validate(positions.get(loc))){
                    cont.put(loc, false);
                } else if (cont.get(loc)){
                    moves.add(new ChessMove(mPosition, positions.get(loc), null));
                    if (mBoard.getPiece(positions.get(loc)) != null){
                        cont.put(loc, false);
                    }
                }
            }
        }
        return moves;
    }
}