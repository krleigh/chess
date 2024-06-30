package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator(){
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPiece.PieceType piece = board.getPiece(myPosition).getPieceType();
        if (piece == ChessPiece.PieceType.BISHOP) {
            BishopMove bMoves = new BishopMove();
            moves = bMoves.pieceMoves(board, myPosition);
        }
        return moves;
    }
}
