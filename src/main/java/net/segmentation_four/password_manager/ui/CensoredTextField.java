package net.segmentation_four.password_manager.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CensoredTextField extends TextField {
    private String text;
    private PasswordFeedback onUpdate;

    /**
     * Constructor
     * @param layout The Layout
     * @param position The Position
     * @param limit The max input length
     */
    public CensoredTextField(Layout layout, Position position, Window window, int limit) {
        super("", layout, position, window, limit);
        this.text = "";
    }

    public CensoredTextField(Layout layout, Position position, Window window, int limit, PasswordFeedback onUpdate) {
        super("", layout, position, window, limit);
        this.text = "";
        this.onUpdate = onUpdate;
    }

    @Override
    public String getText() {
        return text;
    }

    /**
     * Method to limit the number of characters
     * @return A LimitDocument
     */
    @Override
    protected Document createDefaultModel() {
        return new CensoredLimitDocument(this);
    }

    /**
     * Inner class for limiting number of characters
     */
    private class CensoredLimitDocument extends PlainDocument {
        CensoredTextField reference;

        public CensoredLimitDocument(CensoredTextField reference) {
            this.reference = reference;
        }

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

            if((getLength() + str.length()) <= limit) {
                super.insertString(offset, "*".repeat(str.length()), attr);
                text = text.substring(0, offset) + str + text.substring(offset);
            }

            if(onUpdate != null) onUpdate.update(reference);
            window.refresh();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            text = text.substring(0, offs) + text.substring(offs + len);

            if(onUpdate != null) onUpdate.update(reference);
            window.refresh();
        }
    }
}