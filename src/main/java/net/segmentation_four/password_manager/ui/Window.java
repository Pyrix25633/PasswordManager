package net.segmentation_four.password_manager.ui;

import net.segmentation_four.password_manager.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Class to create a window
 * @author Pyrix25633
 * @version 1.0.0
 */
public class Window extends JFrame {
    private static final String iconPath = "./.resources/icon.png";

    /**
     * The Panel
     */
    private final Panel panel;

    /**
     * Constructor
     * @param title The title
     * @param dimension The Dimension
     */
    public Window(String title, Dimension dimension) {
        super();
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(dimension);
        this.panel = new Panel();
        this.add(panel);
        this.setIconImage(ResourceLoader.loadImage(iconPath));
        this.setVisible(true);
    }

    /**
     * Method to request the focus
     */
    @Override
    public void requestFocus() {
        super.requestFocus();
        this.panel.requestFocus();
    }

    /**
     * Method to add a Positionable Component to the Window's Panel
     * @param positionable The Positionable Component
     * @param <T> Type that extends Component and implements Positionable
     */
    public <T extends Component & Positionable> void addToPanel(T positionable) {
        this.panel.addPositionable(positionable);
        this.panel.repaint();
    }

    /**
     * Setter for the Window's Panel background Color
     * @param color The new background Color
     */
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        if(this.panel != null)
            this.panel.setBackground(color);
    }

    /**
     * Method to repaint the Window's Panel
     */
    public void refresh() {
        this.panel.repaint();
        this.panel.repaint();
    }
}