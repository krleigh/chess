package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PawnMove extends MoveCalculator{

    public PawnMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int i = 1;
        if (mColor == ChessGame.TeamColor.BLACK){
            i = -1;
        }

        positions.put(Location.F, new ChessPosition(mRow + i, mCol));
        positions.put(Location.F2, new ChessPosition(mRow + (2*i), mCol));
        positions.put(Location.FL, new ChessPosition(mRow + i, mCol - i));
        positions.put(Location.FR, new ChessPosition(mRow + i, mCol + i));


        List<Location> locations = Arrays.asList(Location.F, Location.FL, Location.FR);

        List<ChessPiece.PieceType> promPieces = Arrays.asList(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK);

        for (Location loc : locations){
            if (validate(positions.get(loc))){
                if (loc == Location.F && mBoard.getPiece(positions.get(loc)) == null ||
                        loc != Location.F && mBoard.getPiece(positions.get(loc)) != null){
                    if (mColor == ChessGame.TeamColor.WHITE && positions.get(loc).getRow() + 1 == 8 ||
                    mColor == ChessGame.TeamColor.BLACK && positions.get(loc).getRow() + 1 == 1){
                        for (ChessPiece.PieceType piece : promPieces){
                            moves.add(new ChessMove(mPosition, positions.get(loc), piece));
                        }
                    } else{
                        moves.add(new ChessMove(mPosition, positions.get(loc), null));
                    }
                }
            }
        }

        if (mColor == ChessGame.TeamColor.WHITE && mRow == 2 || mColor == ChessGame.TeamColor.BLACK && mRow == 7){
            if (mBoard.getPiece(positions.get(Location.F)) == null && mBoard.getPiece(positions.get(Location.F2)) == null){
                moves.add(new ChessMove(mPosition, positions.get(Location.F2), null));
            }
        }
        return moves;
    }
}