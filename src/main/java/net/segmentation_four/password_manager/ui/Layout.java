package net.segmentation_four.password_manager.ui;

/**
 * Class that handles a layout
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Layout {
    // Inner classes

    /**
     * Enum for handling an Horizontal Layout
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
     * Enum for handling a Vertical Layout
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

    // Fields

    /**
     * The Horizontal Layout
     */
    public final Horizontal horizontal;
    /**
     * The Vertical Layout
     */
    public final Vertical vertical;

    // Constructors

    /**
     * Constructor
     */
    public Layout() {
        this(Horizontal.CENTER, Vertical.CENTER);
    }

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