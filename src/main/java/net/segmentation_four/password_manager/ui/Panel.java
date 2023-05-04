package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class that handles a panel
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Panel extends JPanel {
    // Fields

    private final ArrayList<Positionable> positionables;
    private Color color;

    //Constructors

    /**
     * Constructor
     */
    public Panel() {
        super();
        this.positionables = new ArrayList<>();
        this.setLayout(null);
        this.color = GraphicUserInterface.BACKGROUND;
    }

    // Public methods

    /**
     * Adds a Positionable Component to the Panel
     * @param positionable The Positionable Component to add
     * @param <T> Type that extends Component and implements Positionable
     */
    public <T extends Component & Positionable> void addPositionable(T positionable) {
        positionables.add(positionable);
        this.add(positionable);
    }

    /**
     * Sets the background Color
     * @param color The new background Color
     */
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        this.color = color;
    }

    /**
     * Repaints the Panel
     */
    @Override
    public void repaint() {
        if(positionables == null) return;
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
        super.repaint();
    }
}