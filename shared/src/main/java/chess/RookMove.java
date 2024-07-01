package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMove {

    public RookMove(){

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

        boolean F = true;
        boolean L = true;
        boolean R = true;
        boolean B = true;

        for (int i = 1; i < 9; i++){

            ChessPosition Fpos = new ChessPosition(row+i, col);
            if (validate(board, Fpos, myPosition)== false){
                F = false;
            }
            if (F==true){
                moves.add(new ChessMove(myPosition, Fpos, null));
                if (board.getPiece(Fpos) != null){
                    F=false;
                }
            }

            ChessPosition Lpos = new ChessPosition(row, col-i);
            if (validate(board, Lpos, myPosition)== false){
                L = false;
            }
            if (L==true) {moves.add(new ChessMove(
                    myPosition, Lpos, null));
                if (board.getPiece(Lpos) != null){
                    L=false;
                }
            }

            ChessPosition Rpos = new ChessPosition(row, col+i);
            if (validate(board, Rpos, myPosition)== false){
                R = false;
            }
            if (R==true) {
                moves.add(new ChessMove(myPosition, Rpos, null));
                if (board.getPiece(Rpos) != null){
                    R=false;
                }
            }

            ChessPosition Bpos = new ChessPosition(row-i, col);
            if (validate(board, Bpos, myPosition)== false){
                B = false;
            }
            if (B==true) {
                moves.add(new ChessMove(myPosition, Bpos, null));
                if (board.getPiece(Bpos) != null){
                    B=false;
                }
            }

        }

        return moves;
    }
}
