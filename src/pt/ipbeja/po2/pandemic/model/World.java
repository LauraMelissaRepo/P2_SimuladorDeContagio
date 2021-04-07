package pt.ipbeja.po2.pandemic.model;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {
    public static final Random rand = new Random();

    private View view;
    public Cell[][] cells;
    private final int nLines;
    private final int nCols;
    private int minTime;
    private int maxTime;
    private int probContg;
    private int probImm;
    public Map<SickPerson, Integer> mapTime = new HashMap<>();
    private boolean stopGame;
    public int numberOfSick;
    public int numberOfHealthy;
    public int numberOfImmune;

    public Cell[][] getCells() {
        return this.cells;
    }

    /***
     * Constructor to input without a file
     * @param view
     * @param nLines
     * @param nCols
     * @param nHealthy
     * @param nSick
     * @param nImmune
     * @param minTime
     * @param maxTime
     * @param pctg
     * @param pimm
     */
    public World(View view, int nLines, int nCols, int nHealthy, int nSick, int nImmune, int minTime, int maxTime, int pctg, int pimm) {
        this.view = view;
        this.nLines = nLines;
        this.nCols = nCols;
        this.cells = new Cell[this.nLines][this.nCols];
        this.emptyCells();
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.probContg = pctg;
        this.probImm = pimm;
        this.randomPosition(nHealthy, nSick, nImmune, minTime, maxTime);
        printBoard();
    }

    /**
     * Constructor to input with a file
     * @param view
     * @param healthyPersons
     * @param immunePersons
     * @param sickPersons
     * @param nLines
     * @param nCols
     */
    public World(View view, ArrayList<CellPosition> healthyPersons, ArrayList<CellPosition> immunePersons, ArrayList<CellPosition> sickPersons, int nLines, int nCols) {
        this.view = view;
        this.nLines = nLines;
        this.nCols = nCols;
        this.cells = new Cell[this.nLines][this.nCols];
        this.emptyCells();
        this.minTime = 1;
        this.maxTime = 5;
        this.probContg = 100;
        this.probImm = 0;
        setPersons(healthyPersons, immunePersons, sickPersons);
        sickTime(minTime, maxTime);
        printBoard();
    }

    /**
     * Method to set the persons with the coordinates that are in the file
     * @param healthyPersons
     * @param immunePersons
     * @param sickPersons
     */
    public void setPersons(ArrayList<CellPosition> healthyPersons, ArrayList<CellPosition> immunePersons, ArrayList<CellPosition> sickPersons) {
        int putPersonLine;
        int putPersonCol;
        for (int i = 0; i < healthyPersons.size(); i++) {
            putPersonLine = healthyPersons.get(i).getLine();
            putPersonCol = healthyPersons.get(i).getCol();
            this.cells[putPersonLine][putPersonCol] = new HealthyPerson(new CellPosition(putPersonLine, putPersonCol));
        }
        for (int i = 0; i < immunePersons.size(); i++) {
            putPersonLine = immunePersons.get(i).getLine();
            putPersonCol = immunePersons.get(i).getCol();
            this.cells[putPersonLine][putPersonCol] = new ImmunePerson(new CellPosition(putPersonLine, putPersonCol));
        }
        for (int i = 0; i < sickPersons.size(); i++) {
            putPersonLine = sickPersons.get(i).getLine();
            putPersonCol = sickPersons.get(i).getCol();
            this.cells[putPersonLine][putPersonCol] = new SickPerson(new CellPosition(putPersonLine, putPersonCol));
            mapTime.put((SickPerson) this.cells[putPersonLine][putPersonCol], sickTime(minTime, maxTime));
        }
    }

    public void start() {
        new Thread(() -> {
            this.populate();
            this.simulate(100);
        }).start();
    }

    public int nLines() {
        return this.nLines;
    }

    public int nCols() {
        return this.nCols;
    }

    /**
     * Place all Cell positions to Empty
     */
    private void emptyCells() {
        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                this.cells[i][j] = new EmptyCell(new CellPosition(i, j));
            }
        }
    }


    /**
     * Method that calls others functions to put the persons on the board.
     *
     * @param nHealthy
     * @param nSick
     * @param nImmune
     * @param minTime
     * @param maxTime
     */
    private void randomPosition(int nHealthy, int nSick, int nImmune, int minTime, int maxTime) {
        ArrayList<CellPosition> sameLineCol = new ArrayList<>();
        this.personType(CellType.HEALTHYPERSON, nHealthy, sameLineCol);
        this.personType(CellType.IMMUNEPERSON, nImmune, sameLineCol);
        this.personType(CellType.SICKPERSON, nSick, sameLineCol);
        this.sickTime(minTime, maxTime);
    }


    /**
     * Method that depending on the cell type and the value passed as a parameter, puts people on the board.
     * @param ct
     * @param n
     * @param sameLineCol
     */
    private void personType(CellType ct, int n, ArrayList sameLineCol) {
        int randX;
        int randY;
        for (int i = 0; i < n; i++) {
            randX = rand.nextInt(this.nLines);
            randY = rand.nextInt(this.nCols);
            switch (ct) {
                case HEALTHYPERSON:
                    while (this.samePosition(sameLineCol, this.cells[randX][randY].cellPosition)) {
                        randX = rand.nextInt(5);
                        randY = rand.nextInt(5);
                    }
                    this.cells[randX][randY] = new HealthyPerson(new CellPosition(randX, randY));
                    sameLineCol.add(this.cells[randX][randY].cellPosition);
                    break;
                case IMMUNEPERSON:
                    while (this.samePosition(sameLineCol, this.cells[randX][randY].cellPosition)) {
                        randX = rand.nextInt(5);
                        randY = rand.nextInt(5);
                    }
                    this.cells[randX][randY] = new ImmunePerson(new CellPosition(randX, randY));
                    sameLineCol.add(this.cells[randX][randY].cellPosition);
                    break;
                case SICKPERSON:
                    while (this.samePosition(sameLineCol, this.cells[randX][randY].cellPosition)) {
                        randX = rand.nextInt(5);
                        randY = rand.nextInt(5);
                    }
                    this.cells[randX][randY] = new SickPerson(new CellPosition(randX, randY));
                    sameLineCol.add(this.cells[randX][randY].cellPosition);
                    mapTime.put((SickPerson) this.cells[randX][randY], sickTime(minTime, maxTime));
                    break;
                default:
            }
        }
    }

    /**
     * Method that generates a random number between the values ​​given by the user.
     *
     * @param minTime
     * @param maxTime
     * @return the time that a person will be sick
     */
    private int sickTime(int minTime, int maxTime) {
        int randTime;
        randTime = rand.nextInt(maxTime) + minTime;

        return randTime;
    }


    private void populate() {
        for (Cell[] cells : this.cells) {
            for (Cell cell : cells) {
                if (cell.getCellType() != CellType.EMPTYCELL) {
                    view.populateWorld(cell.cellPosition());
                }
            }
        }
    }

    /**
     * Method that traverses the board and calls other methods
     * @param nIter
     */
    private void    simulate(int nIter) {
        for (int i = 0; i < nIter; i++) {
            ArrayList<CellPosition> lastMove = new ArrayList<>();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (stopGame) {
                stopGame = false;
                break;
            }
            for (int j = 0; j < this.nLines; j++) {
                for (int k = 0; k < this.nCols; k++) {
                    if (!this.samePosition(lastMove, this.cells[j][k].cellPosition())) {
                        if (this.cells[j][k].getCellType() != CellType.EMPTYCELL) {
                            cells[j][k].randomMove(this.cells);
                            int newLine = this.cells[j][k].cellPosition().getLine();
                            int newCol = this.cells[j][k].cellPosition().getCol();
                            lastMove.add(this.updateModel(j, k, newLine, newCol));
                            this.view.updatePosition(j, k, this.cells[newLine][newCol].cellPosition().getLine() - j, this.cells[newLine][newCol].cellPosition().getCol() - k, i);
                            this.checkBoardState(probImm);
                        }
                    }
                }
            }
            this.view.setCells(cells);
            this.decreaseSickTime();
            this.printBoard();
        }
    }

    /**
     * Check if the current position is within the arrayList
     *
     * @param lastMove
     * @param cellP
     * @return true if the position is not inside the arrayList
     */
    private boolean samePosition(ArrayList<CellPosition> lastMove, CellPosition cellP) {
        return lastMove.contains(cellP);
    }

    /**
     * Change the state from the old position to Empty and put the person in the new position
     *
     * @param oldLinePosition
     * @param oldColPosition
     * @param newLinePosition
     * @param newColPosition
     * @return the new position
     */
    public CellPosition updateModel(int oldLinePosition, int oldColPosition, int newLinePosition, int newColPosition) {
        Cell cell = this.cells[oldLinePosition][oldColPosition];
        Cell temp;

        temp = cell;
        this.cells[oldLinePosition][oldColPosition] = new EmptyCell(new CellPosition(oldLinePosition, oldColPosition));
        this.cells[newLinePosition][newColPosition] = temp;
        System.out.println("Nova Posição " + this.cells[newLinePosition][newColPosition].getCellType() + " Posições: " + newLinePosition + ", " + newColPosition);

        return cells[newLinePosition][newColPosition].cellPosition();
    }

    /***
     * Method that checks the status of the board for different conditions.
     If there is a sick person on the board, call the makeSick() method.
     Check if the time of the people on the mapTime map has already reached 0, if yes, change the cell type to healthy.
     */
    public void checkBoardState(int probImm) {
        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                if (this.cells[i][j].getCellType() == CellType.SICKPERSON) {
                    this.makeSick(i, j, probContg);
                    if (this.mapTime.get(this.cells[i][j]) == 0) {
                        int odd = (int)(100.0 * Math.random());
                        this.mapTime.remove(this.cells[i][j]);
                        if(odd < probImm) {
                            this.cells[i][j] = new ImmunePerson(this.cells[i][j].cellPosition());
                        }
                        else {
                            this.cells[i][j] = new HealthyPerson(this.cells[i][j].cellPosition());
                        }
                    }
                }
            }
        }
    }


    /***
     * Method that changes the cell type of a healthy person to a sick person,
     * after verifying that both were in juxtaposed positions.
     * @param j
     * @param k
     */
    private void makeSick(int j, int k, int probContg) {
        for (int i = j - 1; i < j + 2; i++) {
            for (int l = k - 1; l < k + 2; l++) {
                if (this.isInside(i, l) && this.cells[i][l].getCellType() == CellType.HEALTHYPERSON) {
                    int odd = (int)(100.0 * Math.random());
                    if(odd < probContg) {
                        this.cells[i][l] = new SickPerson(this.cells[i][l].cellPosition());
                        this.mapTime.put((SickPerson) this.cells[i][l], this.sickTime(this.minTime, this.maxTime));
                    }
                }
            }
        }
    }

    /***
     * Method that decreases the time that a person gets sick
     */
    private void decreaseSickTime() {
        for (SickPerson sickPerson : this.mapTime.keySet()) {
            Integer time = this.mapTime.get(sickPerson);
            this.mapTime.replace(sickPerson, --time);
        }
    }

    /***
     * Method to see if a position is inside of the board
     * @param line
     * @param col
     * @return true if the position is inside
     */
    private boolean isInside(int line, int col) {
        return (this.nLines > line && this.nCols > col && line >= 0 && col >= 0);
    }

    /**
     * Method change the variable to stop the game
     */
    public void stopGame() {
        this.stopGame = true;
    }

    /**
     * Method that saves the state of the board in a string to save in the file
     * @return the string with the positions
     */
    public String writeFile() {
        StringBuilder s = new StringBuilder("");
        s.append(this.nLines).append("\n");
        s.append(this.nCols).append("\n");
        StringBuilder healthy = new StringBuilder("Healthy\n");
        StringBuilder immune = new StringBuilder("Immune\n");
        StringBuilder sick = new StringBuilder("Sick\n");

        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                if (this.cells[i][j].getCellType() == CellType.HEALTHYPERSON) {
                    healthy.append(i).append(" " + j + "\n");
                }
                else if (this.cells[i][j].getCellType() == CellType.IMMUNEPERSON) {
                    immune.append(i).append(" " + j + "\n");
                }
                else if (this.cells[i][j].getCellType() == CellType.SICKPERSON) {
                    sick.append(i).append(" " + j + "\n");
                }
            }
        }
        s.append(healthy).append(immune).append(sick);
        return s.toString();
    }

    /**
     * Print the board on the console and saves the number of people
     */
    public void printBoard() {
        String s = "";
        numberOfSick = 0;
        numberOfHealthy = 0;
        numberOfImmune = 0;
        for (int i = 0; i < this.nLines; i++) {
            for (int j = 0; j < this.nCols; j++) {
                if (this.cells[i][j].getCellType() == CellType.EMPTYCELL) {
                    s += "-";
                }
                if (this.cells[i][j].getCellType() == CellType.SICKPERSON) {
                    s += "S";
                    numberOfSick++;
                }
                else if (this.cells[i][j].getCellType() == CellType.IMMUNEPERSON) {
                    s += "I";
                    numberOfImmune++;
                }
                else if (this.cells[i][j].getCellType() == CellType.HEALTHYPERSON) {
                    s += "H";
                    numberOfHealthy++;
                }
            }
            s += "\n";
        }
        System.out.println("SICK PERSONS: " + numberOfSick + " HEALYHT : " + numberOfHealthy + " IMMUNI : " + numberOfImmune);
        System.out.println(s);
    }
}