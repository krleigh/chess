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
        } else if (piece == ChessPiece.PieceType.KING) {
            KingMove kMoves = new KingMove();
            moves = kMoves.pieceMoves(board, myPosition);
        } else if (piece == ChessPiece.PieceType.QUEEN) {
            QueenMove qMoves = new QueenMove();
            moves = qMoves.pieceMoves(board, myPosition);
        } else if (piece == ChessPiece.PieceType.ROOK) {
            RookMove rMoves = new RookMove();
            moves = rMoves.pieceMoves(board, myPosition);
        } else if (piece == ChessPiece.PieceType.KNIGHT) {
            KnightMove nMoves = new KnightMove();
            moves = nMoves.pieceMoves(board, myPosition);
        }  else if (piece == ChessPiece.PieceType.PAWN) {
            PawnMove pMoves = new PawnMove();
            moves = pMoves.pieceMoves(board, myPosition);
        }
        return moves;
    }
}
