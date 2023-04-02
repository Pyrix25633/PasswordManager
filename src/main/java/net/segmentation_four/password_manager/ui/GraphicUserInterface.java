package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.encryption.Security;
import net.segmentation_four.password_manager.encryption.UserFile;
import net.segmentation_four.password_manager.util.ResourceLoader;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class GraphicUserInterface implements UserInterface {
    public static final Color FOREGROUND = new Color(0xfaf8f6);
    public static final Color BACKGROUND = new Color(0x1d1c1a);
    public static final Color ERROR = new Color(0xff6961);
    public static final Color SUCCESS = new Color(0x77dd77);
    public static final Color BUTTON = new Color(0x7ea4b3);

    @Override
    public synchronized String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Password Manager - Login", new Dimension(450, 380));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-120, -100));
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(70, -100), window, 16);
        Label passwordFeedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -50));
        Label tfaCodeLabel = new Label("2FA code:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-120, 0));
        TextField tfaCodeField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(70, 0), window, 16);
        Label tfaCodeFeedback = new Label("Insert 2FA code", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 50));
        Button loginButton = new Button("Login", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 100));
        loginButton.addActionListener((ActionEvent actionEvent) -> {
            synchronized(this) {
                String passwordText = passwordField.getText();
                try {
                    boolean passwordCorrect = Security.SHA512Hash(passwordText).equals(userFile.getHash());
                    boolean tfaCodeCorrect = Security.getTOTPCode(userFile.getTfaKey()).equals(tfaCodeField.getText().replace(" ", ""));
                    if(passwordCorrect) {
                        password.set(passwordText);
                        passwordFeedback.setText("Correct password");
                        passwordFeedback.setForeground(SUCCESS);
                    }
                    else {
                        passwordFeedback.setText("Wrong password!");
                        passwordFeedback.setForeground(ERROR);
                    }
                    if(tfaCodeCorrect) {
                        tfaCodeFeedback.setText("Correct 2FA code");
                        tfaCodeFeedback.setForeground(SUCCESS);
                    }
                    else {
                        tfaCodeFeedback.setText("Wrong 2FA code!");
                        tfaCodeFeedback.setForeground(ERROR);
                    }
                    if(passwordCorrect && tfaCodeCorrect) notifyAll();
                    window.refresh();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        window.addToPanel(passwordLabel);
        window.addToPanel(passwordField);
        window.addToPanel(passwordFeedback);
        window.addToPanel(tfaCodeLabel);
        window.addToPanel(tfaCodeField);
        window.addToPanel(tfaCodeFeedback);
        window.addToPanel(loginButton);
        window.refresh();
        wait();
        window.dispose();
        return password.get();
    }

    @Override
    public synchronized String getNewPassword(String tfaKey) throws InterruptedException, WriterException {
        Security.createGoogleAuthenticatorQRCode(tfaKey);
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Password Manager - New Password", new Dimension(450, 480));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-120, -160));
        Label feedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -110));
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(70, -160), window, 16,
                (String text) -> {
                    String message = Security.passwordFeedback(text);
                    feedback.setText(message);
                    feedback.setForeground(message.contains("!") ? ERROR : SUCCESS);
                });
        Label tfaLabel = new Label("2FactorAuthentication QR:",
                new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER), new Position(0, -10));
        BufferedImage image = ResourceLoader.loadImage(Security.TFA_QR_PATH);
        BufferedImage qr = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < qr.getWidth(); x++) {
            for(int y = 0; y < qr.getHeight(); y++) {
                if(image.getRGB(x, y) == 0xffffffff)
                    qr.setRGB(x, y, FOREGROUND.getRGB());
                else qr.setRGB(x, y, BACKGROUND.getRGB());
            }
        }
        Image tfaImage = new Image(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER), new Position(0, 100),
                qr);
        Button loginButton = new Button("Set Password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -60));
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
        window.addToPanel(tfaLabel);
        window.addToPanel(tfaImage);
        window.addToPanel(loginButton);
        window.refresh();
        wait();
        window.dispose();
        new File(Security.TFA_QR_PATH).delete();
        return password.get();
    }
}