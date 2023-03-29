package net.segmentation_four.password_manager;

import net.segmentation_four.password_manager.encryption.Guardian;
import net.segmentation_four.password_manager.gui.Label;
import net.segmentation_four.password_manager.gui.Layout;
import net.segmentation_four.password_manager.gui.Position;
import net.segmentation_four.password_manager.gui.TextField;
import net.segmentation_four.password_manager.gui.Window;

import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Main class
 * @author Segmentaton Four
 * @version 1.0.0
 */
public class Main {
    /**
     * Program entrypoint
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        /*
        Window loginWindow = new Window("Password Manager - Login", new Dimension(400, 300));
        loginWindow.setBackground(new Color(100, 100, 100));
        loginWindow.addToPanel(new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-70, 0)));
        loginWindow.addToPanel(new TextField("password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(70, 0)));
        loginWindow.refresh();
        loginWindow.refresh();
        */
        Guardian guardian = new Guardian();
    }
}