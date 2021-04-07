package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

import java.util.Objects;

public class CellPosition {
    private int line;
    private int col;

    public CellPosition(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPosition cellPosition = (CellPosition) o;
        return line == cellPosition.line &&
                col == cellPosition.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, col);
    }

    @Override
    public String toString() {
        return "CellPosition{" +
                "line=" + line +
                ", col=" + col +
                '}';
    }
}