/**
 * Author: Loc Tung Su and Fermin Ramos
 * Email: locsu@unm.edu and ramosfer@unm.edu
 * Class: Cs 351L
 * Professor: Brooke Chenoweth
 * Project 3: domino_gui
 * A Dimension type file that will store information
 * of the dimension if the config file has dimension type
 **/
package disease_simulation;
public class Dimension {
    private int width;

    private int height;
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
}
