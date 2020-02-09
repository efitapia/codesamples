package com.github.efitapia.codesamples.voximplant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@EqualsAndHashCode
public class Cell {
    private static final List<String> ROWS = List.of("1", "2", "3", "4", "5", "6", "7", "8");
    private static final List<String> COLUMNS = List.of("A", "B", "C", "D", "E", "F", "G", "H");

    private final ReentrantLock lock = new ReentrantLock();

    @Getter
    private final int row;

    @Getter
    private final int column;

    @Getter
    private final String coordinates;

    private volatile Rook rook;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.coordinates = ROWS.get(row) + COLUMNS.get(column);
    }

    public void clear() {
        if (!lock.isHeldByCurrentThread()) {
            throw new IllegalStateException(Thread.currentThread().getName() +
                    " clear " + getCoordinates() + " FAIL: lock is not held by current thread");
        }
        this.rook = null;
        lock.unlock();
    }

    public void putRook(Rook rook) {
        if (!lock.isHeldByCurrentThread()) {
            throw new IllegalStateException(Thread.currentThread().getName() +
                    " put on " + getCoordinates() + " FAIL: lock is not held by current thread");
        }
        this.rook = rook;
    }

    public boolean tryLock() {
        if (lock.isHeldByCurrentThread()) {
            throw new IllegalStateException(lockMessage() + " REPEATED");
        }

        boolean success = lock.tryLock();

        log.debug(success ? lockMessage() : lockMessage() + " FAIL");
        return success;
    }

    public void unlock() {
        if (this.rook != null) {
            throw new IllegalStateException(unlockMessage() + " FAIL: cell is busy");
        }

        lock.unlock();
        log.debug(unlockMessage());
    }

    private String lockMessage() {
        return Thread.currentThread().getName() + " lock " + getCoordinates();
    }

    private String unlockMessage() {
        return Thread.currentThread().getName() + " unlock " + getCoordinates();
    }

}
