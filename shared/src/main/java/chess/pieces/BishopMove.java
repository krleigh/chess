package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BishopMove extends MoveCalculator{

    public BishopMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        for (int i = 1; i < 9; i++){

            positions.put(Location.FL, new ChessPosition(mRow + i, mCol - i));
            positions.put(Location.FR, new ChessPosition(mRow + i, mCol + i));
            positions.put(Location.BL, new ChessPosition(mRow - i, mCol -i));
            positions.put(Location.BR, new ChessPosition(mRow - i, mCol +i));

            List<Location> locations = Arrays.asList(Location.FL, Location.FR, Location.BL, Location.BR);

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