package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMove {

    public QueenMove(){

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

        // Create empty list of possible moves
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        // Store location of current piece
        int row = myPosition.getRow()+1;
        int col = myPosition.getColumn()+1;

        // Possible positions

        boolean FL = true;
        boolean F = true;
        boolean FR = true;
        boolean L = true;
        boolean R = true;
        boolean BL = true;
        boolean B = true;
        boolean BR = true;

        // Check positions

        for (int i = 1; i < 9; i++){
            ChessPosition FLpos = new ChessPosition(row+i, col-i);
            if (validate(board, FLpos, myPosition)== false){
                FL = false;
            }
            if (FL ==true){
                moves.add(new ChessMove(myPosition, FLpos, null));
                if (board.getPiece(FLpos) != null){
                    FL = false;
                }
            }

            ChessPosition Fpos = new ChessPosition(row+i, col);
            if (validate(board, Fpos, myPosition)== false){
                F = false;
            }
            if (F ==true){
                moves.add(new ChessMove(myPosition, Fpos, null));
                if (board.getPiece(Fpos) != null){
                    F = false;
                }
            }

            ChessPosition FRpos = new ChessPosition(row+i, col+i);
            if (validate(board, FRpos, myPosition)== false){
                FR = false;
            }
            if (FR ==true){
                moves.add(new ChessMove(myPosition, FRpos, null));
                if (board.getPiece(FRpos) != null){
                    FR = false;
                }
            }

            ChessPosition Lpos = new ChessPosition(row, col-i);
            if (validate(board, Lpos, myPosition)== false){
                L = false;
            }
            if (L ==true){
                moves.add(new ChessMove(myPosition, Lpos, null));
                if (board.getPiece(Lpos) != null){
                    L = false;
                }
            }

            ChessPosition Rpos = new ChessPosition(row, col+i);
            if (validate(board, Rpos, myPosition)== false){
                R = false;
            }
            if (R ==true){
                moves.add(new ChessMove(myPosition, Rpos, null));
                if (board.getPiece(Rpos) != null){
                    R = false;
                }
            }

            ChessPosition BLpos = new ChessPosition(row-i, col-i);
            if (validate(board, BLpos, myPosition)== false){
                BL = false;
            }
            if (BL ==true){
                moves.add(new ChessMove(myPosition, BLpos, null));
                if (board.getPiece(BLpos) != null){
                    BL = false;
                }
            }

            ChessPosition Bpos = new ChessPosition(row-i, col);
            if (validate(board, Bpos, myPosition)== false){
                B = false;
            }
            if (B ==true){
                moves.add(new ChessMove(myPosition, Bpos, null));
                if (board.getPiece(Bpos) != null){
                    B = false;
                }
            }

            ChessPosition BRpos = new ChessPosition(row-i, col+i);
            if (validate(board, BRpos, myPosition)== false){
                BR = false;
            }
            if (BR ==true){
                moves.add(new ChessMove(myPosition, BRpos, null));
                if (board.getPiece(BRpos) != null){
                    BR = false;
                }
            }
        }


        return moves;
    }
}
