package pt.ipbeja.po2.pandemic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

class WorldTest {

    ViewTest VIEW;
    private World world;
    public Cell[][] cells;

    int nLines = 5;
    int nCols = 5;
    int nHealthy = 2;
    int nSick = 3;
    int nImmune = 1;
    int minTime = 1;
    int maxTime = 5;
    int pctg = 100;
    int pimm = 0;

    @BeforeEach
    void setup() {
        this.VIEW = new ViewTest();
        this.world = new World(VIEW, this.nLines, this.nCols, this.nHealthy, this.nSick, this.nImmune, this.minTime, this.maxTime, this.pctg, this.pimm);
        this.cells = new Cell[this.nLines][this.nCols];

        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                this.cells[i][j] = new EmptyCell(new CellPosition(i, j));
            }
        }
        this.cells[1][1] = new SickPerson(new CellPosition(1, 1));
        this.cells[3][2] = new SickPerson(new CellPosition(3, 2));
        this.cells[2][2] = new SickPerson(new CellPosition(2, 2));
        this.cells[4][1] = new ImmunePerson(new CellPosition(4, 1));
        this.cells[2][3] = new ImmunePerson(new CellPosition(2, 3));
        this.cells[0][3] = new HealthyPerson(new CellPosition(0, 3));
        this.cells[3][0] = new HealthyPerson(new CellPosition(3, 0));
        this.cells[4][0] = new SickPerson(new CellPosition(4, 0));
        this.cells[3][4] = new ImmunePerson(new CellPosition(3, 4));
        world.mapTime.put((SickPerson) this.cells[1][1], 2);
        world.mapTime.put((SickPerson) this.cells[3][2], 2);
        world.mapTime.put((SickPerson) this.cells[4][0], 2);
        world.mapTime.put((SickPerson) this.cells[2][2], 2);
        world.cells = this.cells;
        printBoard();
    }

    @Test
    void moveToEmptyCellTest() {
        assertEquals(CellType.SICKPERSON, this.cells[1][1].getCellType());
        assertEquals(CellType.EMPTYCELL, this.cells[2][1].getCellType());

        printBoard();
        this.world.updateModel(1, 1, 2, 1);

        printBoard();
        assertEquals(CellType.EMPTYCELL, this.cells[1][1].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[2][1].getCellType());
    }

    @Test
    void outOfBoardAbove() {
        assertEquals(CellType.HEALTHYPERSON, this.cells[0][3].getCellType());

        this.cells[0][3].isAPossibleMove(-1, 3, this.cells);

        assertEquals(CellType.HEALTHYPERSON, this.cells[0][3].getCellType());

    }

    @Test
    void outOfBoardLeft() {
        assertEquals(CellType.SICKPERSON, this.cells[4][0].getCellType());

        this.cells[4][0].isAPossibleMove(4, -1, this.cells);

        assertEquals(CellType.SICKPERSON, this.cells[4][0].getCellType());
    }

    @Test
    void outOfBoardRight() {
        assertEquals(CellType.IMMUNEPERSON, this.cells[3][4].getCellType());

        this.cells[3][4].isAPossibleMove(3, 5, this.cells);

        assertEquals(CellType.IMMUNEPERSON, this.cells[3][4].getCellType());

    }

    @Test
    void outOfBoardDown() {
        assertEquals(CellType.IMMUNEPERSON, this.cells[4][1].getCellType());

        this.cells[4][1].isAPossibleMove(5, 1, this.cells);

        assertEquals(CellType.IMMUNEPERSON, this.cells[4][1].getCellType());

    }

    @Test
    void contagiousToSick1() {
        assertEquals(CellType.SICKPERSON, this.cells[3][2].getCellType());
        assertEquals(CellType.HEALTHYPERSON, this.cells[3][0].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[4][0].getCellType());
        assertEquals(CellType.IMMUNEPERSON, this.cells[4][1].getCellType());

        this.world.updateModel(3, 2, 3, 1);

        this.world.checkBoardState(pctg);

        assertEquals(CellType.EMPTYCELL, this.cells[3][2].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[3][1].getCellType());
        assertEquals(CellType.IMMUNEPERSON, this.cells[4][1].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[4][0].getCellType());

        assertEquals(CellType.SICKPERSON, this.cells[3][0].getCellType());

        printBoard();
    }

    @Test
    void contagiousToSick2() {
        assertEquals(CellType.SICKPERSON, this.cells[2][2].getCellType());
        assertEquals(CellType.HEALTHYPERSON, this.cells[0][3].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[1][1].getCellType());
        assertEquals(CellType.IMMUNEPERSON, this.cells[2][3].getCellType());

        this.world.updateModel(2, 2, 1, 2);

        this.world.checkBoardState(pctg);

        assertEquals(CellType.EMPTYCELL, this.cells[2][2].getCellType());
        assertEquals(CellType.SICKPERSON, this.cells[1][2].getCellType());
        assertEquals(CellType.IMMUNEPERSON, this.cells[2][3].getCellType());

        assertEquals(CellType.SICKPERSON, this.cells[1][1].getCellType());

        assertEquals(CellType.SICKPERSON, this.cells[0][3].getCellType());

        printBoard();
    }


    public void printBoard() {
        String s = "";
        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                if (this.cells[i][j].getCellType() == CellType.EMPTYCELL) {
                    s += "-";
                }
                if (this.cells[i][j].getCellType() == CellType.SICKPERSON) {
                    s += "S";
                } else if (this.cells[i][j].getCellType() == CellType.IMMUNEPERSON) {
                    s += "I";
                } else if (this.cells[i][j].getCellType() == CellType.HEALTHYPERSON) {
                    s += "H";
                }
            }
            s += "\n";
        }
        System.out.println(s);
    }


}