package net.segmentation_four.password_manager.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class that handles an image
 */
public class Image extends Component implements Positionable {
    //Fields

    private final Layout layout;
    private final Position position;
    private final BufferedImage image;

    //Constructors

    /**
     * Constructor
     * @param layout The Layout
     * @param position The Position
     * @param image The BufferedImage
     */
    public Image(Layout layout, Position position, BufferedImage image) {
        this.layout = layout;
        this.position = position;
        this.image = image;
    }

    //Public methods

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
     * Gets the width
     * @return The width
     */
    @Override
    public int getWidth() {
        return image.getWidth() / 4;
    }

    /**
     * Gets the height
     * @return The height
     */
    @Override
    public int getHeight() {
        return image.getHeight() / 4;
    }

    /**
     * Paints the component
     * @param g The Graphics helper
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, image.getWidth() / 4, image.getHeight() / 4, null);
    }
}