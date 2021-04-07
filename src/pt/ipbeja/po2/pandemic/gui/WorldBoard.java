package pt.ipbeja.po2.pandemic.gui;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pt.ipbeja.po2.pandemic.model.Cell;
import pt.ipbeja.po2.pandemic.model.CellPosition;
import pt.ipbeja.po2.pandemic.model.CellType;
import pt.ipbeja.po2.pandemic.model.World;



public class WorldBoard extends Pane {

    static public final Color[] STATE_COLORS = {Color.BLUE, Color.RED, Color.GREEN};
    private final int CELL_SIZE;
    private final int nLinesPane;
    private final int nColsPane;
    private final World WORLD;
    private Rectangle[][] rectangles;

    public WorldBoard(World world, int size) {
        this.CELL_SIZE = size;
        this.nLinesPane = world.nLines() * CELL_SIZE;
        this.nColsPane = world.nCols() * CELL_SIZE;
        this.setPrefSize(this.nLinesPane, this.nColsPane);
        this.WORLD = world;
        this.rectangles = new Rectangle[world.nLines()][world.nLines()];
    }

    public void populateWorld(CellPosition position) {
        this.rectangles[position.getLine()][position.getCol()] = this.addRectangle(position);
    }

    /**
     * Method that updates the rectangles position
     * @param oldLine
     * @param oldCol
     * @param xx
     * @param yy
     */
    public void updatePosition(int oldLine, int oldCol, int xx, int yy) {
        int newLine = oldLine + xx;
        int newCol = oldCol + yy;
        Rectangle rec = this.rectangles[oldLine][oldCol];
        this.rectangles[oldLine][oldCol] = this.rectangles[newLine][newCol];
        this.rectangles[newLine][newCol] = rec;
        rec.setX(newCol * CELL_SIZE);
        rec.setY(newLine * CELL_SIZE);
    }


    /**
     * The method changes the color according to the type of person
     * @param position
     * @return the rectangle
     */
    private Rectangle addRectangle(CellPosition position) {
        int line = position.getLine() * CELL_SIZE;
        int col = position.getCol() * CELL_SIZE;
        Rectangle r = new Rectangle(col, line, CELL_SIZE, CELL_SIZE);

        if(this.WORLD.getCells()[position.getLine()][position.getCol()].getCellType() == CellType.HEALTHYPERSON) {
            r.setFill(STATE_COLORS[2]);
        }
        else if(this.WORLD.getCells()[position.getLine()][position.getCol()].getCellType() == CellType.SICKPERSON) {
            r.setFill(STATE_COLORS[1]);
        }
        else {
            r.setFill(STATE_COLORS[0]);
        }

        Platform.runLater( () -> {
            this.getChildren().add(r);
        });
        return r;
    }


    /**
     * Method that clears all the board and puts the new rectangles in each place
     * @param cells
     */
    public void setCells(Cell[][] cells) {
        getChildren().clear();
        for (int i = 0; i < cells.length; i++) {
            Cell[] cell = cells[i];
            for (int j = 0; j < cell.length; j++) {
                Cell c = cell[j];
                CellType cellType = c.getCellType();
                Color color = null;
                if(cellType == CellType.SICKPERSON) {
                    color = STATE_COLORS[1];
                }
                else if(cellType == CellType.HEALTHYPERSON) {
                    color = STATE_COLORS[2];
                }
                else if(cellType == CellType.IMMUNEPERSON){
                    color = STATE_COLORS[0];
                }

                CellPosition p = c.cellPosition();
                Rectangle rectangle = new Rectangle(p.getCol()*CELL_SIZE, p.getLine()*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                rectangle.setFill(color);
                getChildren().add(rectangle);

            }
        }
    }
}
