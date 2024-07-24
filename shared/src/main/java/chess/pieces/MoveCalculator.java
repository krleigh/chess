package chess.pieces;

import chess.*;

import java.util.*;

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

    public ArrayList<ChessMove> addDiagonalMoves(ArrayList<ChessMove> moves, int iterator) {
        for (int i = 1; i < iterator; i++){

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

    public ArrayList<ChessMove> addStraightMoves(ArrayList<ChessMove> moves, int iterator) {
        for (int i = 1; i < iterator; i++){

            positions.put(Location.F, new ChessPosition(mRow + i, mCol));
            positions.put(Location.L, new ChessPosition(mRow, mCol - i));
            positions.put(Location.R, new ChessPosition(mRow, mCol + i));
            positions.put(Location.B, new ChessPosition(mRow - i, mCol));

            List<Location> locations = Arrays.asList(Location.F, Location.L, Location.R, Location.B);

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

    public abstract Collection<ChessMove> moves();
}
