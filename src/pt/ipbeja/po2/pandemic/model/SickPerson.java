package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

import java.util.Objects;

public class SickPerson extends Person{

    private CellType cellType;
    private int timeToBeSick;

    public SickPerson(CellPosition cellPosition, int timeToBeSick) {
        super(cellPosition);
        this.cellType = CellType.SICKPERSON;
        this.timeToBeSick = timeToBeSick;
    }

    public SickPerson(CellPosition cellPosition) {
        super(cellPosition);
        this.cellType = CellType.SICKPERSON;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SickPerson that = (SickPerson) o;
        return timeToBeSick == that.timeToBeSick &&
                cellType == that.cellType && super.cellPosition == that.cellPosition;
    }

    @Override
    public int hashCode() {

        return Objects.hash(cellType, timeToBeSick);
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

}
