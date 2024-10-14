package cz.cuni.mff.ganichk.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class LogicGrid {

    int height;
    int width;
    int mines;
    ImageIcon flag = new ImageIcon((Objects.requireNonNull(getClass().getResource("/flagged.png"))));
    private char[][] logicGrid;
    public char mine = 'B';
    private int index;

    /**
     * Constuctor of the class, calls setReset()
     * @param height, height of the board
     * @param width, width of the board
     * @param mines, number of mines
     */
    public LogicGrid(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        setReset();
    }

    /**
     * Getter for mines
     * @return mines
     */
    public char getMine() {
        return this.mine;
    }

    /**
     * Getter for width
     * @return width of the board
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Getter for height
     * @return height of the board
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter of index, index being the cells without a bomb
     * @return index
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Sets the board, where the mines are going to be using random and then Math.floorDiv
     * and finding the neighbors of its cell
     */
    public void setReset() {
        flag = new ImageIcon((flag.getImage()).getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
        logicGrid = new char[this.height][this.width];
        generateMines();
        for (int i=0; i < this.logicGrid.length; i++) {
            for (int j=0; j<this.logicGrid[0].length; j++) {
                if (this.logicGrid[i][j] != mine) {
                    index++;

                    int neighbor = neighborsOfMines(i, j);
                    if (neighbor != 0) {
                        this.logicGrid[i][j] = (char)(neighbor + 48);
                    } else {
                        this.logicGrid[i][j] = ' ';
                    }
                }
            }
        }
    }

    /**
     * Method to random generate mines on the board
     */
    private void generateMines() {
        Set<Integer> uniqueMineIndexes = new HashSet<>();
        Random random = new Random();
        while (uniqueMineIndexes.size() < mines) {
            int randomIndex = random.nextInt(width * height);
            uniqueMineIndexes.add(randomIndex);
        }

        for (int mineIndex : uniqueMineIndexes) {
            logicGrid[mineIndex / width][mineIndex % width] = this.mine;
        }
    }

    /**
     * Checks if the cell has neighbor mines and returns number of neighbor mines
     * @param row, row of the board
     * @param col, col of the board
     * @return how many neighbor mines a cell has
     */
    public int neighborsOfMines(int row, int col) {
        int neighbor = 0;
        for (int i=row-1; i<=row+1; i++) {
            for (int j=col-1; j<=col+1; j++) {
                if (valueOfCell(i, j, this.mine)) {
                    neighbor++;
                }
            }
        }
        return neighbor;
    }

    /**
     * Checks if coordinates are within the bounds of the board
     * and if the piece is ture in array logicGrid
     * @param row, row of the board
     * @param col, column of the board
     * @param piece, character of the cell(which tell us if it is a bomb, empty space or number )
     * @return true/false
     */
    public boolean valueOfCell(int row, int col, char piece) {
        return isBorder(row, col) && this.logicGrid[row][col] == piece;
    }

    /**
     * border checks if the coordinates are within bounds of the board
     * @param row, row of the board
     * @param col, column of the board
     * @return true/false
     */
    public boolean isBorder(int row, int col) {
        return row >= 0 && row < this.height && col >= 0 && col < this.width;
    }


    /**
     * Return the number of the cell as a string, which indicates how many mines are neighbors
     * @param row, row of the board
     * @param col, column of the board
     * @return the character(number of neighbors) of the cell
     */
    public String numOfNeighbors(int row, int col) {
        return Character.toString(this.logicGrid[row][col]);
    }
}
