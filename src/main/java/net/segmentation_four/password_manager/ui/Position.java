package net.segmentation_four.password_manager.ui;

/**
 * Class that handles a position
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Position {
    // Fields

    private final int x;
    private final int y;

    // Constructors

    /**
     * Constructor
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters

    /**
     * Gets the x field
     * @return The x field
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y field
     * @return The y field
     */
    public int getY() {
        return y;
    }
}