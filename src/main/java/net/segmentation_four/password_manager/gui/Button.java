package net.segmentation_four.password_manager.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class to create a button
 * @author Segmentaton Four
 * @version 1.0.0
 */
public class Button extends JButton implements Positionable {
    /**
     * Static field for the font
     */
    private static final Font font = new Font("JetBrains Mono", Font.PLAIN, 24);
    /**
     * The Layout
     */
    private final Layout layout;
    /**
     * The Position
     */
    private final Position position;

    /**
     * Constructor
     * @param text The String text
     * @param layout The Layout
     * @param position The Position
     */
    public Button(String text, Layout layout, Position position) {
        super(text);
        this.setFont(font);
        this.layout = layout;
        this.position = position;
    }

    /**
     * Setter for x and y
     * @param x The new value for the field x
     * @param y The new value for the field y
     */
    @Override
    public void setXY(int x, int y) {
        Dimension size = this.getPreferredSize();
        this.setBounds(x, y, size.width, size.height);
    }

    /**
     * Getter for field layout
     * @return The field layout
     */
    @Override
    public Layout getPositionableLayout() {
        return layout;
    }

    /**
     * Getter for field position
     * @return The field position
     */
    @Override
    public Position getPosition() {
        return position;
    }
}