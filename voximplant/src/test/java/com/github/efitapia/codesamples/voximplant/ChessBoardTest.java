package com.github.efitapia.codesamples.voximplant;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChessBoardTest {

    private final ChessBoard board = new ChessBoard();

    @Test
    public void getBetween_vertical_forward() {
        Cell a6 = board.getCell(4, 0);
        Cell a8 = board.getCell(7, 0);

        List<Cell> between = board.getBetween(a6, a8);

        assertEquals(3, between.size());

        for (int i = 0; i < between.size(); i++) {
            Cell cell = between.get(i);
            assertEquals(0, cell.getColumn());
            assertEquals(5 + i, cell.getRow());
        }
    }

    @Test
    public void getBetween_vertical_reverse() {
        Cell a6 = board.getCell(4, 0);
        Cell a8 = board.getCell(7, 0);

        List<Cell> between = board.getBetween(a8, a6);

        assertEquals(3, between.size());

        for (int i = 0; i < between.size(); i++) {
            Cell cell = between.get(i);
            assertEquals(0, cell.getColumn());
            assertEquals(6 - i, cell.getRow());
        }
    }

    @Test
    public void getBetween_horizontal_forward() {
        Cell b1 = board.getCell(0, 1);
        Cell f1 = board.getCell(0, 5);

        List<Cell> between = board.getBetween(b1, f1);

        assertEquals(4, between.size());

        for (int i = 0; i < between.size(); i++) {
            Cell cell = between.get(i);
            assertEquals(0, cell.getRow());
            assertEquals(2 + i, cell.getColumn());
        }
    }

    @Test
    public void getBetween_horizontal_reverse() {
        Cell b1 = board.getCell(0, 1);
        Cell f1 = board.getCell(0, 5);

        List<Cell> between = board.getBetween(f1, b1);

        assertEquals(4, between.size());

        for (int i = 0; i < between.size(); i++) {
            Cell cell = between.get(i);
            assertEquals(0, cell.getRow());
            assertEquals(4 - i, cell.getColumn());
        }
    }
}