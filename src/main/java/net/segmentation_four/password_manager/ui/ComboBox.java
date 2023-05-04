package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that handles a combo box
 * @author Segmentation Four
 * @version 1.0.0
 */
public class ComboBox extends JComboBox<String> implements Positionable {
    // Fields

    private final Layout layout;
    private final Position position;

    // Constructors

    /**
     * Constructor
     * @param elements The elements
     * @param layout The Layout
     * @param position The Position
     * @param width The width
     */
    public ComboBox(String[] elements, Layout layout, Position position, int width) {
        super(elements);
        this.setFont(GraphicUserInterface.FONT);
        this.layout = layout;
        this.position = position;
        this.setBackground(GraphicUserInterface.BACKGROUND);
        this.setForeground(GraphicUserInterface.FOREGROUND);
        this.setPreferredSize(new Dimension(width, 40));
        ((JLabel)this.getRenderer()).setHorizontalAlignment(0);
    }

    // Public methods

    @Override
    public void setXY(int x, int y) {
        Dimension size = this.getPreferredSize();
        this.setBounds(x, y, size.width, size.height);
    }

    @Override
    public Layout getPositionableLayout() {
        return layout;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the bounds, overridden to fix the arrow not showing correctly
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width
     * @param height The height
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        Component[] comps = getComponents();
        if (comps != null && comps.length >= 1) {
            Component arrow = comps[0];
            arrow.setSize(20, height);
            arrow.setLocation(width - arrow.getWidth(), 0);
        }
    }
}