package net.segmentation_four.password_manager;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.encryption.*;
import net.segmentation_four.password_manager.ui.CommandLineInterface;
import net.segmentation_four.password_manager.ui.GraphicUserInterface;
import net.segmentation_four.password_manager.ui.UserInterface;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

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
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InterruptedException, WriterException {
        /*String password;
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
            if(Security.SHA512Hash(password).equals(hash)) {
                System.out.println("Password is correct");
            }
            else
                System.out.println("Password is not correct");
            in.close();
        }
        else {
            EncodedOutputFile out = new EncodedOutputFile(userFilePath);
            salt = String.valueOf(new Random().nextLong());
            iv = Security.generateIv();
            //TODO: GUI
            System.out.print("Input new password: ");
            Scanner console = new Scanner(System.in);
            password = console.next();
            hash = Security.SHA512Hash(password);
            out.println(hash);
            out.println(salt);
            out.println(iv.getIV());
            out.close();
        }
        System.out.println("Hash: " + hash);
        System.out.println("Salt: " + salt);
        System.out.println("Iv: " + Arrays.toString(iv.getIV()));
        Security security = new Security(password, salt, iv);
        EncryptedOutputFile out = new EncryptedOutputFile("./.resources/test.acc", security);
        out.println("Test123@#$");
        out.close();
        EncryptedInputFile in = new EncryptedInputFile("./.resources/test.acc", security);
        System.out.println(in.next());
        in.close();*/
        UserInterface userInterface = new ArrayList<>(List.of(args)).contains("nogui") ?
                new CommandLineInterface(System.in, System.out) : new GraphicUserInterface();
        if(!UserFile.exists()) {
            String tfaKey = Security.generateTfaKey();
            UserFile.create(userInterface.getNewPassword(tfaKey), tfaKey);
        }
        System.out.println(userInterface.getPassword());
        userInterface.tfAuthenticate();
    }
}