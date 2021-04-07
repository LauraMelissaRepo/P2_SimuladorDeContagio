package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

public class EmptyCell extends Cell {

    private CellType cellType;

    public EmptyCell(CellPosition cellPosition) {
        super(cellPosition);
        this.cellType = CellType.EMPTYCELL;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }
}
