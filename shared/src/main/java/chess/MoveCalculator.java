package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class MoveCalculator {

    public ChessBoard m_board;
    public ChessPosition m_position;

    public ChessGame.TeamColor m_color;
    public ChessPiece.PieceType m_type;

    public int m_row;
    public int m_col;

    public Map<Location, Boolean> cont = new HashMap<Location, Boolean>();
    public Map<Location, ChessPosition> positions = new HashMap<Location, ChessPosition>();

    public MoveCalculator(ChessBoard board, ChessPosition position) {

        m_board = board;
        m_position = position;

        m_color = board.getPiece(position).getTeamColor();
        m_type = board.getPiece(position).getPieceType();

        m_row = position.getRow() + 1;
        m_col = position.getColumn() + 1;

        for (Location loc : Location.values()){
            cont.put(loc, true);
            positions.put(loc, null);
        }

    }

    public enum Location{
        FL,
        LF,
        F,
        F2,
        FR,
        RF,
        L,
        R,
        BL,
        LB,
        B,
        BR,
        RB
    }

    public boolean validate(ChessPosition movpos){

        int row = movpos.getRow() +1;
        int col = movpos.getColumn() +1;


        if (row < 1 || row > 8 || col < 1 || col > 8){
            return false;
        } else {
            ChessPiece place = m_board.getPiece(movpos);
            if (place != null) {place.getTeamColor();}
            ChessGame.TeamColor myColor = m_color;
            if (m_board.getPiece(movpos) != null && m_color == m_board.getPiece(movpos).getTeamColor()){
                return false;
            } else {
                return true;
            }
        }

    }

    public abstract Collection<ChessMove> moves();
}
