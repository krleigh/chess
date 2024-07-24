package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QueenMove extends MoveCalculator{

    public QueenMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves = addStraightMoves(moves, 9);
        moves = addDiagonalMoves(moves, 9);
        return moves;
    }
}
