package net.segmentation_four.password_manager.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Class that handles a censored text field
 * @author Segmentation Four
 * @version 1.0.0
 */
public class CensoredTextField extends TextField {
    // Fields

    private String text;
    private PasswordFeedback onUpdate;

    // Constructors

    /**
     * Constructor
     * @param layout The Layout
     * @param position The Position
     * @param window The window it has been added to
     * @param limit The max input length
     * @param width The width
     */
    public CensoredTextField(Layout layout, Position position, Window window, int limit, int width) {
        super("", layout, position, window, limit, width);
        this.text = "";
    }

    /**
     * Constructor
     * @param layout The Layout
     * @param position The Position
     * @param window The window it has been added to
     * @param limit The max input length
     * @param width The width
     * @param onUpdate The action executed when the field is modified
     */
    public CensoredTextField(Layout layout, Position position, Window window, int limit, int width,
                             PasswordFeedback onUpdate) {
        this(layout, position, window, limit, width);
        this.onUpdate = onUpdate;
    }

    // Getters

    /**
     * Gets the uncensored text field
     * @return The uncensored text field
     */
    @Override
    public String getText() {
        return text;
    }

    // Protected methods

    @Override
    protected Document createDefaultModel() {
        return new CensoredLimitDocument();
    }

    // Inner classes

    private class CensoredLimitDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
            if(str == null) return;

            if((getLength() + str.length()) <= limit) {
                super.insertString(offset, "*".repeat(str.length()), attr);
                text = text.substring(0, offset) + str + text.substring(offset);
            }

            if(onUpdate != null) onUpdate.update(text);
            window.refresh();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            text = text.substring(0, offs) + text.substring(offs + len);

            if(onUpdate != null) onUpdate.update(text);
            window.refresh();
        }
    }
}