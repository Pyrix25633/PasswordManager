package net.segmentation_four.password_manager.ui;

/**
 * Interface to manage a Positionable Component
 * @author Pyrix25633
 * @version 1.0.0
 */
public interface Positionable {
    /**
     * Setter for x and y
     * @param x The new value for the field x
     * @param y The new value for the field y
     */
    void setXY(int x, int y);

    /**
     * Getter for field layout
     * @return The field layout
     */
    Layout getPositionableLayout();

    /**
     * Getter for field position
     * @return The field position
     */
    Position getPosition();

    /**
     * Getter for the width
     * @return The width
     */
    int getWidth();

    /**
     * Getter for the height
     * @return The height
     */
    int getHeight();
}