package com.github.efitapia.codesamples.voximplant;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

@Slf4j
public class Rook implements Runnable {
    private static int number = 0;

    @Getter
    private final String name;
    private final ChessBoard board;

    private volatile Cell currentCell;

    private int moves = 0;

    public Rook(ChessBoard board, Cell currentCell) {
        this.board = board;
        this.currentCell = currentCell;
        this.name = "rook-" + number++;
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public void run() {
        Thread.currentThread().setName(this.name);
        currentCell.tryLock();

        Random random = new Random();
        while (moves < 50) {
            log.debug("{} move {}", this.name, this.moves);
            boolean succes = false;
            while (!succes) {
                succes = move();
            }
            moves++;
            Thread.sleep(random.nextInt(100) + 200);
        }
    }

    private boolean move() {
        Cell destination = chooseDestination();
        log.info("{} trying {} -> {}", this.name, currentCell.getCoordinates(), destination.getCoordinates());

        List<Cell> cellsInTheWay = board.getBetween(currentCell, destination);
        List<Cell> locked = paveTheWay(cellsInTheWay);

        if (!cellsInTheWay.equals(locked)) {
            log.info("{} changing direction...", this.name);
            locked.forEach(Cell::unlock);
            return false;
        }

        destination.putRook(this);
        currentCell.clear();
        currentCell = destination;

        locked.remove(currentCell);
        locked.forEach(Cell::unlock);

        log.info("{} is now at {}", this.name, currentCell.getCoordinates());
        return true;
    }

    private Cell chooseDestination() {
        Random random = new Random();

        boolean isHorizontal = random.nextBoolean();

        int currentCoordinate = isHorizontal ? currentCell.getColumn() : currentCell.getRow();
        int nextCoordinate = currentCoordinate;

        while (nextCoordinate == currentCoordinate) {
            nextCoordinate = random.nextInt(7);
        }

        return isHorizontal
                ? board.getCell(currentCell.getRow(), nextCoordinate)
                : board.getCell(nextCoordinate, currentCell.getColumn());
    }

    @SneakyThrows(InterruptedException.class)
    private List<Cell> paveTheWay(List<Cell> cellsInTheWay) {
        List<Cell> locked = new ArrayList<>();

        for (Cell cell : cellsInTheWay) {
            if (!cell.tryLock()) {
                Thread.sleep(5000);
                if (!cell.tryLock()) {
                    log.info("{} has an obstacle in its way - cell {} seems to be busy", this.name, cell.getCoordinates());
                    break;
                }
            }
            locked.add(cell);
        }
        return locked;
    }

}

