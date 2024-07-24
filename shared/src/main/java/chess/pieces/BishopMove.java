package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BishopMove extends MoveCalculator {

    public BishopMove(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves = addDiagonalMoves(moves, 9);
        return moves;
    }
}