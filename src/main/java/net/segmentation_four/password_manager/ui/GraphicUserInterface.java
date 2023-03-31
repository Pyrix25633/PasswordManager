package net.segmentation_four.password_manager.ui;

import net.segmentation_four.password_manager.encryption.Security;
import net.segmentation_four.password_manager.encryption.UserFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class GraphicUserInterface implements UserInterface {
    private static final Color error = new Color(200, 10, 10);
    private static final Color success = new Color(10, 200, 10);

    @Override
    public synchronized String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Login", new Dimension(450, 300));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-120, -50));
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(60, -50), window, 16);
        Label feedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 0));
        Button loginButton = new Button("Login", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 50));
        loginButton.addActionListener((ActionEvent actionEvent) -> {
            synchronized(this) {
                String text = passwordField.getText();
                try {
                    if(Security.SHA512Hash(text).equals(userFile.getHash())) {
                        password.set(text);
                        notifyAll();
                    }
                    else {
                        feedback.setText("Wrong password!");
                        feedback.setForeground(error);
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        window.addToPanel(passwordLabel);
        window.addToPanel(passwordField);
        window.addToPanel(feedback);
        window.addToPanel(loginButton);
        window.refresh();
        wait();
        window.dispose();
        return password.get();
    }

    @Override
    public synchronized String getNewPassword() throws InterruptedException {
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("New Password", new Dimension(450, 300));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-120, -50));
        Label feedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 0));
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(60, -50), window, 16,
                (CensoredTextField censoredTextField) -> {
                    String message = Security.passwordFeedback(censoredTextField.getText());
                    feedback.setText(message);
                    feedback.setForeground(message.contains("!") ? error : success);
                });
        Button loginButton = new Button("Set Password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 50));
        loginButton.addActionListener((ActionEvent actionEvent) -> {
            synchronized(this) {
                String text = passwordField.getText();
                password.set(text);
                notifyAll();
            }
        });
        window.addToPanel(passwordLabel);
        window.addToPanel(passwordField);
        window.addToPanel(feedback);
        window.addToPanel(loginButton);
        window.refresh();
        wait();
        window.dispose();
        return password.get();
    }
}