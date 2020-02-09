package com.github.efitapia.codesamples.voximplant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessBoard {

    private final Cell[][] board = new Cell[8][8];

    public ChessBoard() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board[row][column] = new Cell(row, column);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return board[row][column];
    }

    public List<Cell> getBetween(Cell current, Cell destination) {
        if (current.getRow() != destination.getRow() && current.getColumn() != destination.getColumn()) {
            throw new IllegalArgumentException("Invalid move " + current.getCoordinates() + " -> " + destination.getCoordinates());
        }

        boolean isHorizontal = current.getRow() == destination.getRow();

        int currentCoordinate;
        int destinationCoordinate;

        if (isHorizontal) {
            currentCoordinate = current.getColumn();
            destinationCoordinate = destination.getColumn();
        } else {
            currentCoordinate = current.getRow();
            destinationCoordinate = destination.getRow();
        }

        int minCoordinate = Math.min(currentCoordinate, destinationCoordinate);
        int maxCoordinate = Math.max(currentCoordinate, destinationCoordinate);

        List<Cell> cellsInBetween = new ArrayList<>();
        if (isHorizontal) {
            for (int i = minCoordinate; i <= maxCoordinate; i++) {
                cellsInBetween.add(board[current.getRow()][i]);
            }
        } else {
            for (int i = minCoordinate; i <= maxCoordinate; i++) {
                cellsInBetween.add(board[i][current.getColumn()]);
            }
        }

        cellsInBetween.remove(current);

        if (currentCoordinate > destinationCoordinate) {
            // so we could lock cells in correct order
            Collections.reverse(cellsInBetween);
        }

        return cellsInBetween;
    }
}
