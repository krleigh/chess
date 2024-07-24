package chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //allocate a new empty 8x8 array of ChessPiece objects
    private ChessPiece[][] mBoard = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        mBoard[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return mBoard[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        mBoard = new ChessPiece[8][8];

        for (ChessGame.TeamColor color : ChessGame.TeamColor.values()){
            int row = 1;
            int row2 = 2;

            if (color == ChessGame.TeamColor.BLACK){
                row = 8;
                row2 = 7;
            }

            for (int i = 1; i < 9; i++){
                addPiece(new ChessPosition(row2, i), new ChessPiece(color, ChessPiece.PieceType.PAWN));
            }

            addPiece(new ChessPosition(row, 1), new ChessPiece(color, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(row, 2), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(row, 3), new ChessPiece(color, ChessPiece.PieceType.BISHOP));

            addPiece(new ChessPosition(row, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
            addPiece(new ChessPosition(row, 5), new ChessPiece(color, ChessPiece.PieceType.KING));

            addPiece(new ChessPosition(row, 8), new ChessPiece(color, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(row, 7), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(row, 6), new ChessPiece(color, ChessPiece.PieceType.BISHOP));

        }
    }

    @Override
    public String toString() {
        ArrayList<String> printBoard = new ArrayList<>();
        for (int i = 8; i > 0; i--){
            for (int j = 1; j < 9; j++){
                var piece = this.getPiece(new ChessPosition(i, j));
                if (piece == null){
                    printBoard.add("|"+ " " + "|");
                } else {
                    printBoard.add(piece.toString());
                }
                if (j == 8){
                    printBoard.add("\n");
                }

            }
        }
        return printBoard.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(this.mBoard, that.mBoard);
    }

    @Override
    public int hashCode() {return Arrays.deepHashCode(mBoard);}

}