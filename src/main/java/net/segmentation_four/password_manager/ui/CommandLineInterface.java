package net.segmentation_four.password_manager.ui;

import net.segmentation_four.password_manager.encryption.Security;
import net.segmentation_four.password_manager.encryption.UserFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CommandLineInterface implements UserInterface {
    private final Scanner in;
    private final PrintStream out;

    public CommandLineInterface(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    @Override
    public String getPassword() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        out.print("Password: ");
        String password = in.nextLine();
        while(!Security.SHA512Hash(password).equals(userFile.getHash())) {
            out.println("Wrong password!");
            out.print("Password: ");
            password = in.nextLine();
        }
        return password;
    }

    @Override
    public String getNewPassword(String tfaKey) {
        out.print("New password: ");
        String password = in.nextLine();
        String message = Security.passwordFeedback(password);
        while(message.contains("!")) {
            out.println(message);
            out.print("New password: ");
            password = in.nextLine();
            message = Security.passwordFeedback(password);
        }
        System.out.println("2FactorAuthentication key: " + tfaKey);
        return password;
    }

    @Override
    public void tfAuthenticate() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        out.print("2FA code: ");
        String tfaCode = in.nextLine();
        while(!Security.getTOTPCode(userFile.getTfaKey()).equals(tfaCode.replace(" ", ""))) {
            out.println("Wrong 2fa code!");
            out.print("2FA code: ");
            tfaCode = in.nextLine();
        }
    }
}