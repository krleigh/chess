package chess;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTests {

    public static void main(String [] args){
//        var board = new ChessBoard();
//        board.resetBoard();
//        for (int i = 1; i < 9; i++){
//            for (int j =1; j < 9; j++){
//                System.out.print(board.getPiece(new ChessPosition(i, j)));
//            }
//        }

//        ChessBoard board1 = new ChessBoard();
//        ChessBoard board2 = new ChessBoard();
//
//        board1.resetBoard();
//        board2.resetBoard();

// Assuming board1 and board2 are initialized and populated

//        System.out.println("Hash code of board1: " + board1.hashCode());
//        System.out.println("Hash code of board2: " + board2.hashCode());
//
//        boolean isEqual = board1.equals(board2);
//        System.out.println("Boards are equal: " + isEqual);
//
//        System.out.println(board1);
//        System.out.println(board2);

//        ChessPosition pos1 = new ChessPosition(1, 2);
//        ChessPosition pos2 = new ChessPosition(1, 2);
//
//        assertEquals(pos1, pos2);
//
//
//        System.out.println(pos1.hashCode());
//        System.out.println(pos2.hashCode());

        ChessBoard board = new ChessBoard();
        board.addPiece(new ChessPosition(5, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        BishopMove bishop = new BishopMove();
        Collection<ChessMove> moves = bishop.pieceMoves(board, new ChessPosition(5,4));
//        System.out.println(moves);


    }
}
