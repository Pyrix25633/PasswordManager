package net.segmentation_four.password_manager.ui;

import net.segmentation_four.password_manager.resource.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Class that handles a window
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Window extends JFrame {
    // Constants

    private static final String ICON_PATH = "icon.png";

    // Fields

    private final Panel panel;

    // Constructors

    /**
     * Constructor
     * @param title The title
     * @param dimension The Dimension
     * @throws IOException IOException
     */
    public Window(String title, Dimension dimension) throws IOException {
        super();
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(dimension);
        this.panel = new Panel();
        this.add(panel);
        this.setIconImage(ResourceLoader.loadImage(ICON_PATH, true, 8));
        this.setVisible(true);
    }

    // Public methods

    /**
     * Request the focus
     */
    @Override
    public void requestFocus() {
        super.requestFocus();
        this.panel.requestFocus();
    }

    /**
     * Adds a Positionable Component to the Window's Panel
     * @param positionable The Positionable Component
     * @param <T> Type that extends Component and implements Positionable
     */
    public <T extends Component & Positionable> void addToPanel(T positionable) {
        this.panel.addPositionable(positionable);
        this.panel.repaint();
    }

    /**
     * Sets the Window's Panel background Color
     * @param color The new background Color
     */
    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        if(this.panel != null)
            this.panel.setBackground(color);
    }

    /**
     * Repaints the Window's Panel
     */
    public void refresh() {
        this.panel.repaint();
        this.panel.repaint();
    }
}