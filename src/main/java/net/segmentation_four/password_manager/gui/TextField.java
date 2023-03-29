package net.segmentation_four.password_manager.gui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Class to create a text field
 * @author Segmentaton Four
 * @version 1.0.0
 */
public class TextField extends JTextField implements Positionable {
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
     * The input length limit
     */
    private final int limit;

    /**
     * Constructor
     * @param text The String text
     * @param layout The Layout
     * @param position The Position
     */
    public TextField(String text, Layout layout, Position position) {
        super();
        this.setFont(font);
        this.layout = layout;
        this.position = position;
        this.limit = 16;
        this.setText(text);
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

    /**
     * Method to limit the number of characters
     * @return A LimitDocument
     */
    @Override
    protected Document createDefaultModel() {
        return new LimitDocument();
    }

    /**
     * Inner class for limiting number of characters
     */
    private class LimitDocument extends PlainDocument {
        /**
         * Method to add a string to the document if it fits
         * @param offset The offset
         * @param str The String
         * @param attr The AttributeSet
         * @throws BadLocationException If the location is not valid
         */
        @Override
        public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
            if(str == null) return;

            if((getLength() + str.length()) <= limit)
                super.insertString(offset, str, attr);
        }
    }
}