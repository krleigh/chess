package ui;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static int GAMEID;



    public static void DrawBoard(int gameID) {
        GAMEID = gameID;
    }

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setGrey(out);

        String[] headers = { EMPTY, A, B, C, D, E, F, G, H, EMPTY };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
            if (boardCol == BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }


//            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
//                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
//            }
        }

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
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setGrey(out);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (boardCol == 0 || boardCol == 9) {
                    setGrey(out);
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                } else {
                    drawSquares(out, boardRow, boardCol);
                }




            }
            setBlack(out);
            out.println();
        }
    }

    private static void drawSquares(PrintStream out, int boardRow, int boardCol) {
        out.print(SET_TEXT_COLOR_WHITE);
        if (boardCol % 2 == 0 && boardRow %2 == 0 || boardCol % 2 != 0 && boardRow % 2 != 0) {
            setWhite(out);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        } else {
            setBlack(out);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
    }



    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGrey(PrintStream out) {
        out.print (SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }


}
