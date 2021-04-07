package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

public interface View {
    void populateWorld(CellPosition position);

    void updatePosition(int oldLine, int oldCol, int xx, int yy, int i);

    void setCells(Cell[][] cells);
}
