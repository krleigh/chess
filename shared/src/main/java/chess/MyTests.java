package chess;

public class MyTests {

    public static void main(String [] args){
//        var board = new ChessBoard();
//        board.resetBoard();
//        for (int i = 1; i < 9; i++){
//            for (int j =1; j < 9; j++){
//                System.out.print(board.getPiece(new ChessPosition(i, j)));
//            }
//        }

        ChessBoard board1 = new ChessBoard();
        ChessBoard board2 = new ChessBoard();

        board1.resetBoard();
        board2.resetBoard();

// Assuming board1 and board2 are initialized and populated

        System.out.println("Hash code of board1: " + board1.hashCode());
        System.out.println("Hash code of board2: " + board2.hashCode());

        boolean isEqual = board1.equals(board2);
        System.out.println("Boards are equal: " + isEqual);

        System.out.println(board1);
        System.out.println(board2);
    }
}
