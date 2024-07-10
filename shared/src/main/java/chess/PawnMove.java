package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PawnMove extends MoveCalculator{

    public PawnMove(ChessBoard board, ChessPosition position){
        super(board, position);
    }

    public Collection<ChessMove> moves(){
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int i = 1;
        if (m_color == ChessGame.TeamColor.BLACK){
            i = -1;
        }

        positions.put(Location.F, new ChessPosition(m_row + i, m_col));
        positions.put(Location.F2, new ChessPosition(m_row + (2*i), m_col));
        positions.put(Location.FL, new ChessPosition(m_row + i, m_col - i));
        positions.put(Location.FR, new ChessPosition(m_row + i, m_col + i));


        List<Location> locations = Arrays.asList(Location.F, Location.FL, Location.FR);

        List<ChessPiece.PieceType> promPieces = Arrays.asList(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK);

        for (Location loc : locations){
            if (validate(positions.get(loc))){
                if (loc == Location.F && m_board.getPiece(positions.get(loc)) == null ||
                        loc != Location.F && m_board.getPiece(positions.get(loc)) != null){
                    if (m_color == ChessGame.TeamColor.WHITE && positions.get(loc).getRow() + 1 == 8 ||
                    m_color == ChessGame.TeamColor.BLACK && positions.get(loc).getRow() + 1 == 1){
                        for (ChessPiece.PieceType piece : promPieces){
                            moves.add(new ChessMove(m_position, positions.get(loc), piece));
                        }
                    } else{
                        moves.add(new ChessMove(m_position, positions.get(loc), null));
                    }

                }

            }
        }

        if (m_color == ChessGame.TeamColor.WHITE && m_row == 2 || m_color == ChessGame.TeamColor.BLACK && m_row == 7){
            if (m_board.getPiece(positions.get(Location.F)) == null && m_board.getPiece(positions.get(Location.F2)) == null){
                moves.add(new ChessMove(m_position, positions.get(Location.F2), null));
            }
        }

        return moves;
    }
}