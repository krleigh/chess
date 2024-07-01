package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMove {

    public PawnMove(){

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
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        System.out.println(color + " " + row + ", " + col);

        // Possible positions

        boolean FL = true;
        boolean F = true;
        boolean FR = true;

        int i = 1;
        if (color == ChessGame.TeamColor.BLACK){
            i = -1;
        }

        ChessPosition FLpos = new ChessPosition(row+i, col-i);
        ChessPosition Fpos = new ChessPosition(row+i, col);
        ChessPosition FRpos = new ChessPosition(row+i, col+i);

        ChessPosition F2pos = new ChessPosition(row+(2*i), col);

        // Check positions




        if (validate(board, FLpos, myPosition) == true){
            if (board.getPiece(FLpos) != null && board.getPiece(FLpos).getTeamColor() != color){
                if(color == ChessGame.TeamColor.BLACK && FLpos.getRow()+1==1 || color == ChessGame.TeamColor.WHITE && FLpos.getRow()+1 == 8) {
                    moves.add(new ChessMove(myPosition, FLpos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, FLpos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, FLpos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, FLpos, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, FLpos, null));
                }
            }
        }

        if (validate(board, Fpos, myPosition)== false || board.getPiece(Fpos) != null){
            F = false;
        }
        if (F == true){
            if (board.getPiece(Fpos) == null) {

                if(color == ChessGame.TeamColor.BLACK && Fpos.getRow()+1==1 || color == ChessGame.TeamColor.WHITE && Fpos.getRow()+1 == 8) {
                    moves.add(new ChessMove(myPosition, Fpos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, Fpos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, Fpos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, Fpos, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, Fpos, null));
                }
            } else {
                F = false;
            }
        }

        if (validate(board, FRpos, myPosition) == true){
            if (board.getPiece(FRpos) != null && board.getPiece(FRpos).getTeamColor() != color){
                if(color == ChessGame.TeamColor.BLACK && FRpos.getRow()+1==1 || color == ChessGame.TeamColor.WHITE && FRpos.getRow()+1 == 8) {
                    moves.add(new ChessMove(myPosition, FRpos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, FRpos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, FRpos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, FRpos, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, FRpos, null));
                }

            }
        }
        if (color == ChessGame.TeamColor.BLACK && row ==7 && board.getPiece(F2pos) == null && F == true) {
            moves.add(new ChessMove(myPosition, F2pos, null));
        }

        if (color == ChessGame.TeamColor.WHITE && row ==2 && board.getPiece(F2pos) == null && F == true) {
            moves.add(new ChessMove(myPosition, F2pos, null));
        }

        return moves;
    }
}
