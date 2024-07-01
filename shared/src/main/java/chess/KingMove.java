package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove {

    public KingMove(){

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

        ChessPosition FLpos = new ChessPosition(row+1, col-1);
        ChessPosition Fpos = new ChessPosition(row+1, col);
        ChessPosition FRpos = new ChessPosition(row+1, col+1);
        ChessPosition Lpos = new ChessPosition(row, col-1);
        ChessPosition Rpos = new ChessPosition(row, col+1);
        ChessPosition BLpos = new ChessPosition(row-1, col-1);
        ChessPosition Bpos = new ChessPosition(row-1, col);
        ChessPosition BRpos = new ChessPosition(row-1, col+1);

        // Check positions
        if (validate(board, FLpos, myPosition)== false){
            FL = false;
        }
        if (FL ==true){
            moves.add(new ChessMove(myPosition, FLpos, null));
            if (board.getPiece(FLpos) != null){
                FL = false;
            }
        }

        if (validate(board, Fpos, myPosition)== false){
            F = false;
        }
        if (F ==true){
            moves.add(new ChessMove(myPosition, Fpos, null));
            if (board.getPiece(Fpos) != null){
                F = false;
            }
        }

        if (validate(board, FRpos, myPosition)== false){
            FR = false;
        }
        if (FR ==true){
            moves.add(new ChessMove(myPosition, FRpos, null));
            if (board.getPiece(FRpos) != null){
                FR = false;
            }
        }

        if (validate(board, Lpos, myPosition)== false){
            L = false;
        }
        if (L ==true){
            moves.add(new ChessMove(myPosition, Lpos, null));
            if (board.getPiece(Lpos) != null){
                L = false;
            }
        }

        if (validate(board, Rpos, myPosition)== false){
            R = false;
        }
        if (R ==true){
            moves.add(new ChessMove(myPosition, Rpos, null));
            if (board.getPiece(Rpos) != null){
                R = false;
            }
        }

        if (validate(board, BLpos, myPosition)== false){
            BL = false;
        }
        if (BL ==true){
            moves.add(new ChessMove(myPosition, BLpos, null));
            if (board.getPiece(BLpos) != null){
                BL = false;
            }
        }

        if (validate(board, Bpos, myPosition)== false){
            B = false;
        }
        if (B ==true){
            moves.add(new ChessMove(myPosition, Bpos, null));
            if (board.getPiece(Bpos) != null){
                B = false;
            }
        }

        if (validate(board, BRpos, myPosition)== false){
            BR = false;
        }
        if (BR ==true){
            moves.add(new ChessMove(myPosition, BRpos, null));
            if (board.getPiece(BRpos) != null){
                BR = false;
            }
        }

        return moves;
    }
}
