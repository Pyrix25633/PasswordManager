package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class for handling a label
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Label extends JLabel implements Positionable {
    // Fields

    private final Layout layout;
    private final Position position;

    // Constructors

    /**
     * Constructor
     * @param text The text
     * @param layout The Layout
     * @param position The Position
     */
    public Label(String text, Layout layout, Position position) {
        super(text);
        this.setFont(GraphicUserInterface.FONT);
        this.layout = layout;
        this.position = position;
        this.setForeground(GraphicUserInterface.FOREGROUND);
    }

    /**
     * Constructor
     * @param text The text
     * @param layout The Layout
     * @param position The Position
     * @param width The width
     */
    public Label(String text, Layout layout, Position position, int width) {
        this(text, layout, position);
        this.setOpaque(true);
        this.setBackground(GraphicUserInterface.ITEM_BACKGROUND);
        this.setPreferredSize(new Dimension(width, 40));
        this.setHorizontalAlignment(0);
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
}