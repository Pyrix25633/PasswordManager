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
    private Scanner in;
    private PrintStream out;

    public CommandLineInterface(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    @Override
    public String getPassword() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        out.print("Password: ");
        String password = in.next();
        while(!Security.SHA512Hash(password).equals(userFile.getHash())) {
            out.println("Wrong password!");
            out.print("Password: ");
            password = in.next();
        }
        return password;
    }

    @Override
    public String getNewPassword() {
        out.print("New password: ");
        String password = in.next();
        String message = Security.passwordFeedback(password);
        while(message.contains("!")) {
            out.println(message);
            out.print("New password: ");
            password = in.next();
            message = Security.passwordFeedback(password);
        }
        System.out.println(message);
        return password;
    }
}