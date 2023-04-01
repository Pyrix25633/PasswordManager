package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class to create a label
 * @author Pyrix25633
 * @version 1.0.0
 */
public class Label extends JLabel implements Positionable {
    /**
     * Static field for the font
     */
    private static final Font font = new Font("JetBrains Mono", Font.PLAIN, 24);
    /**
     * The Layout
     */
    private final Layout layout;
    /**
     * The Layout
     */
    private final Position position;

    /**
     * Constructor
     * @param text The text
     * @param layout The Layout
     * @param position The Position
     */
    public Label(String text, Layout layout, Position position) {
        super(text);
        this.setFont(font);
        this.layout = layout;
        this.position = position;
        this.setForeground(GraphicUserInterface.FOREGROUND);
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