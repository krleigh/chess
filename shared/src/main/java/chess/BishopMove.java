package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMove {

    public BishopMove(){

    }

    private boolean validate(ChessBoard board, ChessPosition move, ChessPosition myPosition){
        if (move.getRow()+1 > 8 || move.getColumn()+1 >8 || move.getRow()+1 < 1 || move.getColumn()+1 < 1){
            return false;
        }else {
            if (board.getPiece(move) != null && board.getPiece(move).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                return false;
            }
            return true;
        }
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int row = myPosition.getRow()+1;
        int col = myPosition.getColumn()+1;

        ArrayList<ChessPosition> lookAt = new ArrayList<ChessPosition>();

        boolean FL = true;
        boolean FR = true;
        boolean BL = true;
        boolean BR = true;

        for (int i = 1; i < 9; i++){

            ChessPosition FLpos = new ChessPosition(row+i, col-i);
            if (validate(board, FLpos, myPosition)== false){
                FL = false;
            }
            if (FL==true){
                moves.add(new ChessMove(myPosition, FLpos, null));
                if (board.getPiece(FLpos) != null){
                    FL=false;
                }
            }

            ChessPosition FRpos = new ChessPosition(row+i, col+i);
            if (validate(board, FRpos, myPosition)== false){
                FR = false;
            }
            if (FR==true) {moves.add(new ChessMove(
                    myPosition, FRpos, null));
                    if (board.getPiece(FRpos) != null){
                        FR=false;
                    }
            }

            ChessPosition BLpos = new ChessPosition(row-i, col-i);
            if (validate(board, BLpos, myPosition)== false){
                BL = false;
            }
            if (BL==true) {
                moves.add(new ChessMove(myPosition, BLpos, null));
                if (board.getPiece(BLpos) != null){
                    BL=false;
                }
            }

            ChessPosition BRpos = new ChessPosition(row-i, col+i);
            if (validate(board, BRpos, myPosition)== false){
                BR = false;
            }
            if (BR==true) {
                moves.add(new ChessMove(myPosition, BRpos, null));
                if (board.getPiece(BRpos) != null){
                    BR=false;
                }
            }

        }

        return moves;
    }
}
