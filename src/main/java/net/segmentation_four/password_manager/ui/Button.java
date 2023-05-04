package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class that handles a button
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Button extends JButton implements Positionable {
    // Fields

    private final Layout layout;
    private final Position position;

    // Constructors

    /**
     * Constructor
     * @param text The String text
     * @param layout The Layout
     * @param position The Position
     * @param width The width
     */
    public Button(String text, Layout layout, Position position, int width) {
        super(text);
        this.setFont(GraphicUserInterface.FONT);
        this.layout = layout;
        this.position = position;
        this.setBackground(GraphicUserInterface.BUTTON);
        this.setForeground(GraphicUserInterface.BACKGROUND);
        this.setBorderPainted(false);
        this.setPreferredSize(new Dimension(width, 40));
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