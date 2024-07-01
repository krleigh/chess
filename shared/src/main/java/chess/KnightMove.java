package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMove {

    public KnightMove(){

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

        int row = myPosition.getRow() + 1;
        int col = myPosition.getColumn() + 1;

        boolean FL = true;
        boolean LF = true;
        boolean FR = true;
        boolean RF = true;
        boolean BL = true;
        boolean LB = true;
        boolean BR = true;
        boolean RB = true;


        ChessPosition FLpos = new ChessPosition(row+2, col-1);
        if (validate(board, FLpos, myPosition)== false){
            FL = false;
        }
        if (FL==true){
            moves.add(new ChessMove(myPosition, FLpos, null));
            if (board.getPiece(FLpos) != null){
                FL=false;
            }
        }

        ChessPosition LFpos = new ChessPosition(row+1, col-2);
        if (validate(board, LFpos, myPosition)== false){
            LF = false;
        }
        if (LF==true){
            moves.add(new ChessMove(myPosition, LFpos, null));
            if (board.getPiece(LFpos) != null){
                LF=false;
            }
        }

        ChessPosition FRpos = new ChessPosition(row+2, col+1);
        if (validate(board, FRpos, myPosition)== false){
            FR = false;
        }
        if (FR==true) {moves.add(new ChessMove(
                myPosition, FRpos, null));
            if (board.getPiece(FRpos) != null){
                FR=false;
            }
        }

        ChessPosition RFpos = new ChessPosition(row+1, col+2);
        if (validate(board, RFpos, myPosition)== false){
            RF = false;
        }
        if (RF==true){
            moves.add(new ChessMove(myPosition, RFpos, null));
            if (board.getPiece(RFpos) != null){
                RF=false;
            }
        }

        ChessPosition BLpos = new ChessPosition(row-2, col-1);
        if (validate(board, BLpos, myPosition)== false){
            BL = false;
        }
        if (BL==true) {
            moves.add(new ChessMove(myPosition, BLpos, null));
            if (board.getPiece(BLpos) != null){
                BL=false;
            }
        }

        ChessPosition LBpos = new ChessPosition(row-1, col-2);
        if (validate(board, LBpos, myPosition)== false){
            LB = false;
        }
        if (LB==true) {
            moves.add(new ChessMove(myPosition, LBpos, null));
            if (board.getPiece(LBpos) != null){
                LB=false;
            }
        }

        ChessPosition BRpos = new ChessPosition(row-2, col+1);
        if (validate(board, BRpos, myPosition)== false){
            BR = false;
        }
        if (BR==true) {
            moves.add(new ChessMove(myPosition, BRpos, null));
            if (board.getPiece(BRpos) != null){
                BR=false;
            }
        }

        ChessPosition RBpos = new ChessPosition(row-1, col+2);
        if (validate(board, RBpos, myPosition)== false){
            RB = false;
        }
        if (RB==true) {
            moves.add(new ChessMove(myPosition, RBpos, null));
            if (board.getPiece(RBpos) != null){
                RB=false;
            }
        }

        return moves;
    }

}
