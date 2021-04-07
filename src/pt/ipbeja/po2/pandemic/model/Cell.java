package pt.ipbeja.po2.pandemic.model;

import pt.ipbeja.po2.pandemic.gui.ContagiousBoard;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */


public abstract class Cell {
    protected CellPosition cellPosition;
    private int dx;
    private int dy;
    private int nLines = ContagiousBoard.LINES;
    private int nCols = ContagiousBoard.COLS;


    public Cell(CellPosition cellPosition) {
        this.cellPosition = cellPosition;
    }

    public CellPosition cellPosition() {
        return cellPosition;
    }

    /**
     * Method that creates the new random position of the person
     * @param cells
     */
    public void randomMove(Cell [][] cells) {
        final int[] v = {-1, 0, 1};
        this.dx = v[World.rand.nextInt(3)];
        this.dy = v[World.rand.nextInt(3)];
        if ((dx == 0 && dy == 0)) {// to force a move
            dx = 1;
        }
        int x = this.cellPosition.getLine() + dx;
        int y = this.cellPosition.getCol() + dy;

        isAPossibleMove(x, y, cells);
    }

    /**
     * Method that checks if the new positions are valid
     * @param x
     * @param y
     * @param cells
     */
    public void isAPossibleMove(int x, int y, Cell [][] cells) {
        if((nLines > x && nCols > y && x >= 0 && y >= 0) && (cells[x][y].getCellType() == CellType.EMPTYCELL)) {
            this.cellPosition = new CellPosition(x, y);
        }
    }

    public abstract CellType getCellType();
}