package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

public class ImmunePerson extends Person {

    private CellType cellType;

    public ImmunePerson(CellPosition cellPosition) {
        super(cellPosition);
        this.cellType = CellType.IMMUNEPERSON;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }
}
