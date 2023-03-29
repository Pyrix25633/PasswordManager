package net.segmentation_four.password_manager;

import net.segmentation_four.password_manager.encryption.EncodedInputFile;
import net.segmentation_four.password_manager.encryption.EncodedOutputFile;
import net.segmentation_four.password_manager.encryption.Guardian;
import net.segmentation_four.password_manager.gui.Label;
import net.segmentation_four.password_manager.gui.Layout;
import net.segmentation_four.password_manager.gui.Position;
import net.segmentation_four.password_manager.gui.TextField;
import net.segmentation_four.password_manager.gui.Window;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class
 * @author Pyrix25633
 * @version 1.0.0
 */
public class Main {
    private static final String userFilePath = "./.resources/user.pm";

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
        String password;
        BigInteger hash;
        String salt;
        IvParameterSpec iv;
        if(new File(userFilePath).exists()) {
            EncodedInputFile in = new EncodedInputFile(userFilePath);
            hash = in.nextBigInteger();
            salt = in.next();
            iv = new IvParameterSpec(in.nextBytes());
            //TODO: GUI
            System.out.print("Input password: ");
            Scanner console = new Scanner(System.in);
            password = console.next();
            if(Guardian.SHA512Hash(password).equals(hash)) {
                System.out.println("Password is correct");
                System.out.println("Hash: " + hash);
                System.out.println("Salt: " + salt);
                System.out.println("Iv: " + Arrays.toString(iv.getIV()));
            }
            else
                System.out.println("Password is not correct");
            in.close();
        }
        else {
            EncodedOutputFile out = new EncodedOutputFile(userFilePath);
            salt = String.valueOf(new Random().nextLong());
            iv = Guardian.generateIv();
            //TODO: GUI
            System.out.print("Input new password: ");
            Scanner console = new Scanner(System.in);
            password = console.next();
            hash = Guardian.SHA512Hash(password);
            System.out.println("Hash: " + hash);
            System.out.println("Salt: " + salt);
            System.out.println("Iv: " + Arrays.toString(iv.getIV()));
            out.println(hash);
            out.println(salt);
            out.println(iv.getIV());
            out.close();
        }
        Guardian guardian = new Guardian(password, salt, iv);
    }
}