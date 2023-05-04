package net.segmentation_four.password_manager.ui;

/**
 * Interface that handles a Positionable Component
 * @author Segmentation Four
 * @version 1.0.0
 */
public interface Positionable {
    // Public methods

    /**
     * Sets x and y fields
     * @param x The new value for the x field
     * @param y The new value for the y field
     */
    void setXY(int x, int y);

    /**
     * Gets the layout field
     * @return The layout field
     */
    Layout getPositionableLayout();

    /**
     * Gets the position field
     * @return The position field
     */
    Position getPosition();

    /**
     * Gets the width
     * @return The width
     */
    int getWidth();

    /**
     * Gets the height
     * @return The height
     */
    int getHeight();
}