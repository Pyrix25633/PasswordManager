package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.Main;
import net.segmentation_four.password_manager.encryption.AccountFile;
import net.segmentation_four.password_manager.encryption.Security;
import net.segmentation_four.password_manager.encryption.UserFile;
import net.segmentation_four.password_manager.resource.ResourceLoader;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class for handling graphic user interface
 * @author Segmentation Four
 * @version 1.0.0
 */
public class GraphicUserInterface implements UserInterface {
    //Constants
    /**
     * Foreground color
     */
    public static final Color FOREGROUND = new Color(0xfaf8f6);
    /**
     * Background color
     */
    public static final Color BACKGROUND = new Color(0x1d1c1a);
    /**
     * Background color for items
     */
    public static final Color ITEM_BACKGROUND = new Color(0x2d3033);
    /**
     * Foreground color for error messages
     */
    public static final Color ERROR = new Color(0xff6961);
    /**
     * Foreground color for success messages
     */
    public static final Color SUCCESS = new Color(0x77dd77);
    /**
     * Background color for buttons
     */
    public static final Color BUTTON = new Color(0xa3b8cc);
    /**
     * Interface font
     */
    public static final Font FONT;

    static {
        try {
            FONT = ResourceLoader.loadFont("JetBrainsMono-Regular.ttf", 24);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Password Manager - Login", new Dimension(720, 350));
        Label passwordLabel = new Label("Password:", new Layout(), new Position(-240, -100), 160);
        CensoredTextField passwordField = new CensoredTextField(new Layout(),
                new Position(90, -100), window, 32, 460);
        Label passwordFeedback = new Label("Insert Password", new Layout(), new Position(0, -50));
        Label tfaCodeLabel = new Label("2FA Code:", new Layout(), new Position(-65, 0), 510);
        TextField tfaCodeField = new TextField("", new Layout(),
                new Position(265, 0), window, 7, 110);
        Label tfaCodeFeedback = new Label("Insert 2FA Code", new Layout(), new Position(0, 50));
        Button loginButton = new Button("Login", new Layout(), new Position(0, 100), 110);
        loginButton.addActionListener((ActionEvent actionEvent) -> {
            synchronized(this) {
                String passwordText = passwordField.getText();
                try {
                    boolean passwordCorrect = Security.SHA512Hash(passwordText).equals(userFile.getHash());
                    boolean tfaCodeCorrect = Security.getTOTPCode(userFile.getTfaKey()).equals(tfaCodeField.getText().replace(" ", ""));
                    if(passwordCorrect) {
                        password.set(passwordText);
                        passwordFeedback.setText("Correct Password");
                        passwordFeedback.setForeground(SUCCESS);
                    } else {
                        passwordFeedback.setText("Wrong Password!");
                        passwordFeedback.setForeground(ERROR);
                    }
                    if(tfaCodeCorrect) {
                        tfaCodeFeedback.setText("Correct 2FA Code");
                        tfaCodeFeedback.setForeground(SUCCESS);
                    } else {
                        tfaCodeFeedback.setText("Wrong 2FA Code!");
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
    public synchronized String getNewPassword(String tfaKey, int defaultCloseOperation) throws InterruptedException, WriterException, IOException {
        Security.createAuthenticatorQRCode(tfaKey);
        AtomicReference<String> password = new AtomicReference<>();
        Window window = new Window("Password Manager - New Password", new Dimension(720, 470));
        window.setDefaultCloseOperation(defaultCloseOperation);
        Label passwordLabel = new Label("Password:", new Layout(), new Position(-240, -160), 160);
        Label feedback = new Label("Insert Password", new Layout(), new Position(0, -110));
        CensoredTextField passwordField = new CensoredTextField(new Layout(),
                new Position(90, -160), window, 32, 460,
                (String text) -> {
                    String message = Security.passwordFeedback(text);
                    feedback.setText(message);
                    feedback.setForeground(message.contains("!") ? ERROR : SUCCESS);
                });
        Label tfaLabel = new Label("2FactorAuthentication QR:", new Layout(), new Position(0, -10), 640);
        BufferedImage image = ResourceLoader.loadImage(Security.TFA_QR_PATH, false);
        BufferedImage qr = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < qr.getWidth(); x++) {
            for(int y = 0; y < qr.getHeight(); y++) {
                if(image.getRGB(x, y) == 0xffffffff)
                    qr.setRGB(x, y, FOREGROUND.getRGB());
                else qr.setRGB(x, y, BACKGROUND.getRGB());
            }
        }
        Image tfaImage = new Image(new Layout(), new Position(0, 100), qr);
        Button setPasswordButton = new Button("Set Password", new Layout(), new Position(0, -60), 210);
        setPasswordButton.addActionListener((ActionEvent actionEvent) -> {
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
        window.addToPanel(setPasswordButton);
        window.refresh();
        wait();
        window.dispose();
        new File(Security.TFA_QR_PATH).delete();
        return password.get();
    }

    @Override
    public void main() throws IOException {
        Window window = new Window("Password Manager", new Dimension(810, 800));
        Button changePasswordButton = new Button("Change Password", new Layout(),
                new Position(-185, -325), 360);
        changePasswordButton.addActionListener((ActionEvent actionEvent) -> new Thread(() ->
            {
                try {
                    Main.changePassword();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start());
        Button regenerateKeyButton = new Button("Regenerate Key", new Layout(),
                new Position(190, -325), 350);
        regenerateKeyButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                Main.regenerateKey();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Label createModifyLabel = new Label("Create/Modify Account", new Layout(), new Position(0, -225));
        Label nameLabel1 = new Label("Account Name:", new Layout(), new Position(-240, -175), 250);
        TextField nameField = new TextField("", new Layout(),
                new Position(135, -175), window, 32, 460);
        Label usernameLabel1 = new Label("Username/E-mail:", new Layout(),
                new Position(-240, -125), 250);
        TextField usernameField = new TextField("", new Layout(),
                new Position(135, -125), window, 128, 460);
        Label passwordLabel1 = new Label("Password:", new Layout(), new Position(-240, -75), 250);
        TextField passwordField = new TextField("", new Layout(),
                new Position(135, -75), window, 64, 460);
        Label passwordLengthLabel = new Label("Random Password Length:", new Layout(),
                new Position(-35, -25), 660);
        TextField passwordLengthField = new TextField("", new Layout(),
                new Position(340, -25), window, 2, 50);
        Button saveButton = new Button("Save Account", new Layout(), new Position(-240, 25), 250);
        Button generatePasswordButton = new Button("Generate Random Password", new Layout(),
                new Position(135, 25), 460);
        generatePasswordButton.addActionListener((ActionEvent actionEvent) ->
            passwordField.setText(Security.generatePassword(Integer.parseInt(passwordLengthField.getText()))));
        Label showDeleteLabel = new Label("Show/Delete Account", new Layout(), new Position(0, 125));
        Label nameLabel2 = new Label("Account Name:", new Layout(), new Position(-240, 175), 250);
        ComboBox nameCombo = new ComboBox(AccountFile.getFileList().toArray(new String[0]), new Layout(),
                new Position(135, 175), 460);
        Button showButton = new Button("Show Account", new Layout(), new Position(-190, 225), 350);
        Button deleteButton = new Button("Delete Account", new Layout(),
                new Position(185, 225), 360);
        Label usernameLabel2 = new Label("Username/E-mail:", new Layout(),
                new Position(-240, 275), 250);
        Label showUsernameLabel = new Label("", new Layout(), new Position(105, 275), 400);
        Button copyUsernameButton = new Button("", new Layout(), new Position(345, 275), 40);
        ImageIcon copyIcon = new ImageIcon(ResourceLoader.loadImage("copy.png", true, 2));
        copyUsernameButton.setIcon(copyIcon);
        copyUsernameButton.addActionListener((ActionEvent actionEvent) -> {
            StringSelection username = new StringSelection(showUsernameLabel.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, username);
        });
        Label passwordLabel2 = new Label("Password:", new Layout(), new Position(-240, 325), 250);
        Label showPasswordLabel = new Label("", new Layout(), new Position(105, 325), 400);
        Button copyPasswordButton = new Button("", new Layout(), new Position(345, 325), 40);
        copyPasswordButton.setIcon(copyIcon);
        copyPasswordButton.addActionListener((ActionEvent actionEvent) -> {
            StringSelection password = new StringSelection(showPasswordLabel.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(password, password);
        });
        saveButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                AccountFile.create(nameField.getText(), usernameField.getText(), passwordField.getText());
                nameCombo.removeAllItems();
                for(String account : AccountFile.getFileList())
                    nameCombo.addItem(account);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        showButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                String selected = (String)nameCombo.getSelectedItem();
                if(selected == null) return;
                AccountFile account = new AccountFile(selected);
                showUsernameLabel.setText(account.getUsername());
                showPasswordLabel.setText(account.getPassword());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        deleteButton.addActionListener((ActionEvent actionEvent) -> {
            try {
                String selected = (String)nameCombo.getSelectedItem();
                if(selected == null) return;
                AccountFile.delete(selected);
                nameCombo.removeAllItems();
                for(String account : AccountFile.getFileList())
                    nameCombo.addItem(account);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        window.addToPanel(changePasswordButton);
        window.addToPanel(regenerateKeyButton);
        window.addToPanel(createModifyLabel);
        window.addToPanel(nameLabel1);
        window.addToPanel(nameField);
        window.addToPanel(usernameLabel1);
        window.addToPanel(usernameField);
        window.addToPanel(passwordLabel1);
        window.addToPanel(passwordField);
        window.addToPanel(passwordLengthLabel);
        window.addToPanel(passwordLengthField);
        window.addToPanel(saveButton);
        window.addToPanel(generatePasswordButton);
        window.addToPanel(showDeleteLabel);
        window.addToPanel(nameLabel2);
        window.addToPanel(nameCombo);
        window.addToPanel(showButton);
        window.addToPanel(deleteButton);
        window.addToPanel(usernameLabel2);
        window.addToPanel(showUsernameLabel);
        window.addToPanel(copyUsernameButton);
        window.addToPanel(passwordLabel2);
        window.addToPanel(showPasswordLabel);
        window.addToPanel(copyPasswordButton);
        window.refresh();
    }
}