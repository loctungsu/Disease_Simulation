/**
 * Author: Loc Tung Su and Fermin Ramos
 * Email: locsu@unm.edu and ramosfer@unm.edu
 * Class: Cs 351L
 * Professor: Brooke Chenoweth
 * Project 3: domino_gui
 * A Grid type file that will store information
 * of the grid if the config file has grid type
 **/
package disease_simulation;

public class Grid {
    private int row;
    private int col;
    private int numberOfAgent;
    public Grid(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public Grid (int row, int col, int numberOfAgent) {
        this.row = row;
        this.col = col;
        this.numberOfAgent = numberOfAgent;
    }
    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }
    public int getNumberOfAgent() {
        return this.numberOfAgent;
    }
}
