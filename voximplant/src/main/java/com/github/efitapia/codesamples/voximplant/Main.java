package com.github.efitapia.codesamples.voximplant;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("RANDOM ROOKS");
        int rooksAmount = askForAmount();

        ChessBoard board = new ChessBoard();
        List<Rook> rooks = fillBoard(board, rooksAmount);

        ExecutorService executorService = Executors.newFixedThreadPool(rooksAmount);

        rooks.forEach(executorService::execute);
        executorService.shutdown();
    }

    private static int askForAmount() throws IOException {
        System.out.println("Enter amount of rooks [1-6]");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();

        while (true) {
            try {
                int rooksAmount = Integer.parseInt(input);

                if (rooksAmount < 1 || rooksAmount > 6) {
                    log.error("Invalid input! Enter a single number from 1 to 6");
                } else {
                    return rooksAmount;
                }
            } catch (NumberFormatException e) {
                log.error("Invalid input! Enter a single number from 1 to 6");
            }
        }
    }


    private static List<Rook> fillBoard(ChessBoard board, int rooksAmount) {
        List<Rook> rooks = new ArrayList<>();
        List<Cell> reservedCells = new ArrayList<>();
        for (int i = 0; i < rooksAmount; i++) {
            rooks.add(placeRook(board, reservedCells));
        }

        return rooks;
    }

    private static Rook placeRook(ChessBoard board, List<Cell> reservedCells) {
        Cell cell = chooseCell(board);
        while (reservedCells.contains(cell)) {
            cell = chooseCell(board);
        }
        Rook rook = new Rook(board, cell);

        log.info(rook.getName() + " placed at " + cell.getCoordinates());
        return rook;
    }

    private static Cell chooseCell(ChessBoard board) {
        Random random = new Random();

        int row = random.nextInt(8);
        int column = random.nextInt(8);

        return board.getCell(row, column);
    }


}
