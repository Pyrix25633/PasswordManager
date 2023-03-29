package net.segmentation_four.password_manager.gui;

/**
 * Class to manage a layout
 * @author Pyrix25633
 * @version 1.0.0
 */
public class Layout {
    /**
     * Enum to manage an Horizontal Layout
     */
    public enum Horizontal {
        /**
         * Left
         */
        LEFT,
        /**
         * Center
         */
        CENTER,
        /**
         * Right
         */
        RIGHT
    }

    /**
     * Enum to manage a Vertical Layout
     */
    public enum Vertical {
        /**
         * Top
         */
        TOP,
        /**
         * Center
         */
        CENTER,
        /**
         * Bottom
         */
        BOTTOM
    }

    /**
     * The Horizontal Layout
     */
    public final Horizontal horizontal;
    /**
     * The Vertical Layout
     */
    public final Vertical vertical;

    /**
     * Constructor
     * @param horizontal The Horizontal Layout
     * @param vertical The Vertical Layout
     */
    public Layout(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}