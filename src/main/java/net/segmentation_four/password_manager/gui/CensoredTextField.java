package net.segmentation_four.password_manager.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class CensoredTextField extends TextField {
    private String text;

    /**
     * Constructor
     * @param layout The Layout
     * @param position The Position
     * @param limit The max input length
     */
    public CensoredTextField(Layout layout, Position position, int limit) {
        super("", layout, position, limit);
        this.text = "";
    }

    /**
     * Method to limit the number of characters
     * @return A LimitDocument
     */
    @Override
    protected Document createDefaultModel() {
        return new CensoredLimitDocument();
    }

    /**
     * Inner class for limiting number of characters
     */
    private class CensoredLimitDocument extends PlainDocument {
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
                System.out.println(text);
            }

            repaint();
            repaint();
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            text = text.substring(0, offs) + text.substring(offs + len);

            repaint();
            repaint();
        }
    }
}