package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.Main;
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
    public static final Color ITEM_BACKGROUND = new Color(0x2d3033);
    public static final Color ERROR = new Color(0xff6961);
    public static final Color SUCCESS = new Color(0x77dd77);
    public static final Color BUTTON = new Color(0xa3b8cc);

    @Override
    public synchronized String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Password Manager - Login", new Dimension(720, 350));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-240, -100), 160);
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(90, -100), window, 32, 460);
        Label passwordFeedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -50));
        Label tfaCodeLabel = new Label("2FA code:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-65, 0), 510);
        TextField tfaCodeField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(265, 0), window, 7, 110);
        Label tfaCodeFeedback = new Label("Insert 2FA code", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 50));
        Button loginButton = new Button("Login", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 100), 110);
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
                    } else {
                        passwordFeedback.setText("Wrong password!");
                        passwordFeedback.setForeground(ERROR);
                    }
                    if(tfaCodeCorrect) {
                        tfaCodeFeedback.setText("Correct 2FA code");
                        tfaCodeFeedback.setForeground(SUCCESS);
                    } else {
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
        Window window = new Window("Password Manager - New Password", new Dimension(660, 470));
        Label passwordLabel = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-230, -160));
        Label feedback = new Label("Insert password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -110));
        CensoredTextField passwordField = new CensoredTextField(new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(65, -160), window, 32, 500,
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

    @Override
    public void main() {
        Window window = new Window("Password Manager", new Dimension(660, 800));
        Button changePasswordButton = new Button("Change Password", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-100, -325), 300);
        changePasswordButton.addActionListener((ActionEvent actionEvent) -> new Thread(() ->
            {
                try {
                    Main.changePassword();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start());
        Button regenerateKeyButton = new Button("Regenerate Key", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(150, -325));
        regenerateKeyButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                Main.regenerateKey();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Label createModifyLabel = new Label("Create/Modify account", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, -225));
        Label nameLabel = new Label("Account name:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-235, -175), 250);
        TextField nameField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(75, -175), window, 32, 500);
        Label usernameLabel = new Label("Username/e-mail:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-235, -125));
        TextField usernameField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(75, -125), window, 32, 500);
        Label passwordLabel1 = new Label("Password:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-230, -75));
        TextField passwordField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(65, -75), window, 32, 500);
        Label passwordLengthLabel = new Label("Random Password Length:", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(-150, -25));
        TextField passwordLengthField = new TextField("", new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(65, -125), window, 2, 50);
        window.addToPanel(changePasswordButton);
        window.addToPanel(regenerateKeyButton);
        window.addToPanel(createModifyLabel);
        window.addToPanel(nameLabel);
        window.addToPanel(nameField);
        window.addToPanel(usernameLabel);
        window.addToPanel(usernameField);
        window.addToPanel(passwordLabel1);
        window.addToPanel(passwordField);
        window.addToPanel(passwordLengthLabel);
        window.addToPanel(passwordLengthField);
        window.refresh();
    }
}