package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

public class HealthyPerson extends Person {

    private CellType cellType;

    public HealthyPerson(CellPosition cellPosition) {
        super(cellPosition);
        this.cellType = CellType.HEALTHYPERSON;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }
}
