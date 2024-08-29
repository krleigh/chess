package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    private static GameData game;
    private static ChessGame.TeamColor color;
    ;

    public DrawBoard(GameData game, ChessGame.TeamColor color) {
        DrawBoard.game = game;
        DrawBoard.color = color;
    }


    public static void draw(Collection<ChessPosition> validMoves, ChessPosition position){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out, validMoves, position);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void drawHeaders(PrintStream out) {

        if (color == ChessGame.TeamColor.BLACK) {
            reversibleHeader(out, BOARD_SIZE_IN_SQUARES-1, -1, -1);
        } else {
            reversibleHeader(out, 0, BOARD_SIZE_IN_SQUARES, +1);
        }
    }

    private static void reversibleHeader(PrintStream out, int start, int end, int step) {
        String[] headers = { EMPTY, A, B, C, D, E, F, G, H, EMPTY };

        for (int boardCol = start; boardCol != end; boardCol += step) {
            drawHeader(out, headers[boardCol]);
        }
        setBlack(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

    }

    private static void drawChessBoard(PrintStream out, Collection<ChessPosition> validMoves, ChessPosition position) {
        if (color == ChessGame.TeamColor.BLACK) {
            reversibleChessBoard(out, validMoves, position, BOARD_SIZE_IN_SQUARES-3, BOARD_SIZE_IN_SQUARES-1, -1, -1, -1);
        } else {
            reversibleChessBoard(out, validMoves, position, 0, 0, BOARD_SIZE_IN_SQUARES-2, BOARD_SIZE_IN_SQUARES, +1);
        }
    }
    private static void reversibleChessBoard(PrintStream out, Collection<ChessPosition> validMoves, ChessPosition position,
                                             int startRow, int startCol, int endRow, int endCol, int step) {
        for (int boardRow = startRow; boardRow != endRow; boardRow += step ) {

            for (int boardCol = startCol; boardCol != endCol; boardCol += step) {
                if (boardCol == 0 || boardCol == 9) {
                    out.print(SET_BG_COLOR_DARK_GREY);
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print("\u2003" + (BOARD_SIZE_IN_SQUARES-3-boardRow+1) +  "\u2003");
                } else {
                    drawSquares(out, validMoves, position, BOARD_SIZE_IN_SQUARES-3-boardRow, boardCol);
                }
            }
            setBlack(out);
            out.println();
        }
    }
    private static void drawSquares(PrintStream out, Collection<ChessPosition> validMoves, ChessPosition position,
                                    int boardRow, int boardCol) {

        if (boardCol % 2 == 0 && boardRow %2 == 0 || boardCol % 2 != 0 && boardRow % 2 != 0) {
            setGrey(out);
            if (validMoves != null && validMoves.contains(new ChessPosition(boardRow+1, boardCol))){
                out.print(SET_BG_COLOR_GREEN);
            }
            if(position !=null && Objects.equals(position, new ChessPosition(boardRow+1, boardCol))) {
                out.print(SET_BG_COLOR_BLUE);
            }
            printPiece(out, boardRow, boardCol);
        } else {
            setDarkGrey(out);
            if (validMoves != null && validMoves.contains(new ChessPosition(boardRow+1, boardCol))){
                out.print(SET_BG_COLOR_DARK_GREEN);
            }
            if(position !=null && Objects.equals(position, new ChessPosition(boardRow+1, boardCol))) {
                out.print(SET_BG_COLOR_BLUE);
            }
            printPiece(out, boardRow, boardCol);
        }
    }

    private static void printPiece(PrintStream out, int boardRow, int boardCol){
        ChessPiece piece = game.game().getBoard().getPiece(new ChessPosition(boardRow+1, boardCol));
        String pieceUI = EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS);
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_WHITE);
                switch (piece.getPieceType()) {
                    case PAWN -> pieceUI = WHITE_PAWN;
                    case KNIGHT -> pieceUI = WHITE_KNIGHT;
                    case BISHOP -> pieceUI = WHITE_BISHOP;
                    case ROOK -> pieceUI = WHITE_ROOK;
                    case QUEEN -> pieceUI = WHITE_QUEEN;
                    case KING -> pieceUI = WHITE_KING;
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                out.print(SET_TEXT_COLOR_BLACK);
                switch (piece.getPieceType()) {
                    case PAWN -> pieceUI = BLACK_PAWN;
                    case KNIGHT -> pieceUI = BLACK_KNIGHT;
                    case BISHOP -> pieceUI = BLACK_BISHOP;
                    case ROOK -> pieceUI = BLACK_ROOK;
                    case QUEEN -> pieceUI = BLACK_QUEEN;
                    case KING -> pieceUI = BLACK_KING;
                }
            }
        }

        out.print(pieceUI);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGrey(PrintStream out) {
        out.print (SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setDarkGrey(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);

    }


}
