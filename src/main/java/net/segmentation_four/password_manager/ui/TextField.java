package net.segmentation_four.password_manager.ui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Class for handling a text field
 * @author SegmentationFour
 * @version 1.0.0
 */
public class TextField extends JTextField implements Positionable {
    // Fields

    protected final Layout layout;
    protected final Position position;
    protected final Window window;
    protected final int limit;

    // Constructors

    /**
     * Constructor
     * @param text The String text
     * @param layout The Layout
     * @param position The Position
     * @param window The window it has been added to
     * @param limit The max input length
     * @param width The width
     */
    public TextField(String text, Layout layout, Position position, Window window, int limit, int width) {
        super();
        this.setFont(GraphicUserInterface.FONT);
        this.layout = layout;
        this.position = position;
        this.window = window;
        this.limit = limit;
        this.setText(text);
        this.setBackground(GraphicUserInterface.BACKGROUND);
        this.setForeground(GraphicUserInterface.FOREGROUND);
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

    // Protected methods

    @Override
    protected Document createDefaultModel() {
        return new LimitDocument();
    }

    // Inner classes

    protected class LimitDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
            if(str == null) return;

            if((getLength() + str.length()) <= limit)
                super.insertString(offset, str, attr);

            window.refresh();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            window.refresh();
        }
    }
}