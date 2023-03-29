package net.segmentation_four.password_manager.gui;

/**
 * Class to manage a position
 * @author Pyrix25633
 * @version 1.0.0
 */
public class Position {
    /**
     * The x coordinate
     */
    private final int x;
    /**
     * The y coordinate
     */
    private final int y;

    /**
     * Constructor
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x
     * @return The field x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     * @return The field y
     */
    public int getY() {
        return y;
    }
}