package net.segmentation_four.password_manager.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class to create a panel
 * @author Segmentaton Four
 * @version 1.0.0
 */
public class Panel extends JPanel {
    /**
     * The Positionable Components
     */
    private final ArrayList<Positionable> positionables;
    /**
     * The Color
     */
    private Color color;

    /**
     * Constructor
     */
    public Panel() {
        super();
        this.positionables = new ArrayList<>();
        this.setLayout(null);
        this.color = new Color(0xfdffb6);
    }

    /**
     * Method to add a Positionable Component to the Panel
     * @param positionable The Positionable Component to add
     * @param <T> Type that extends Component and implements Positionable
     */
    public <T extends Component & Positionable> void addPositionable(T positionable) {
        positionables.add(positionable);
        this.add(positionable);
    }

    /**
     * Setter for the background Color
     * @param color The new background Color
     */
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        this.color = color;
    }

    /**
     * Method to repaint the Panel
     */
    @Override
    public void repaint() {
        if(positionables == null) return;
        super.repaint();
        for(Positionable p : positionables) {
            int x = switch(p.getPositionableLayout().horizontal) {
                case LEFT -> p.getPosition().getX();
                case CENTER -> ((getWidth() - p.getWidth()) / 2) + p.getPosition().getX();
                case RIGHT -> getWidth() - p.getWidth() + p.getPosition().getX();
            };
            int y = switch(p.getPositionableLayout().vertical) {
                case TOP -> p.getPosition().getY();
                case CENTER -> ((getHeight() - p.getHeight()) / 2) + p.getPosition().getY();
                case BOTTOM -> getHeight() - p.getHeight() + p.getPosition().getY();
            };
            p.setXY(x, y);
        }
        super.setBackground(color);
    }
}