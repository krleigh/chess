package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class MoveCalculator {

    public ChessBoard mBoard;
    public ChessPosition mPosition;

    public ChessGame.TeamColor mColor;
    public ChessPiece.PieceType mType;

    public int mRow;
    public int mCol;

    public Map<Location, Boolean> cont = new HashMap<Location, Boolean>();
    public Map<Location, ChessPosition> positions = new HashMap<Location, ChessPosition>();

    public MoveCalculator(ChessBoard board, ChessPosition position) {

        mBoard = board;
        mPosition = position;

        mColor = board.getPiece(position).getTeamColor();
        mType = board.getPiece(position).getPieceType();

        mRow = position.getRow() + 1;
        mCol = position.getColumn() + 1;

        for (Location loc : Location.values()){
            cont.put(loc, true);
            positions.put(loc, null);
        }

    }

    public enum Location{
        FL,
        LF,
        F,
        F2,
        FR,
        RF,
        L,
        R,
        BL,
        LB,
        B,
        BR,
        RB
    }

    public boolean validate(ChessPosition movpos){

        int row = movpos.getRow() +1;
        int col = movpos.getColumn() +1;


        if (row < 1 || row > 8 || col < 1 || col > 8){
            return false;
        } else {
            ChessPiece place = mBoard.getPiece(movpos);
            if (place != null) {place.getTeamColor();}
            ChessGame.TeamColor myColor = mColor;
            if (mBoard.getPiece(movpos) != null && mColor == mBoard.getPiece(movpos).getTeamColor()){
                return false;
            } else {
                return true;
            }
        }

    }

    public abstract Collection<ChessMove> moves();
}
