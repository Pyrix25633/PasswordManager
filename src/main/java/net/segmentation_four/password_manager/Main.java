package net.segmentation_four.password_manager;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.encryption.*;
import net.segmentation_four.password_manager.ui.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * Main class
 * @author Segmentation Four
 * @version 1.0.0
 */
public class Main {
    // Static variables

    /**
     * The Security instance that handles the key file encryption and decryption
     */
    public static Security keySecurity;
    /**
     * The Security instance that handles the account files encryption and decryption
     */
    public static Security accountSecurity;
    private static UserInterface userInterface;

    // Public methods

    /**
     * Program entrypoint
     * @param args Command-line arguments
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InterruptedException InterruptedException
     * @throws WriterException WriterException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, InterruptedException, WriterException {
        userInterface = new ArrayList<>(List.of(args)).contains("nogui") ?
                new CommandLineInterface(System.in, System.out) : new GraphicUserInterface();
        Files.createDirectories(Paths.get("./.resources/accounts"));
        if(!UserFile.exists()) {
            String tfaKey = Security.generateTfaKey();
            UserFile.create(userInterface.getNewPassword(tfaKey, JFrame.EXIT_ON_CLOSE), tfaKey);
        }
        UserFile userFile = UserFile.getInstance();
        keySecurity = new Security(userInterface.getPassword(), userFile.getSalt(), userFile.getIv());
        if(!KeyFile.exists())
            KeyFile.create();
        accountSecurity = KeyFile.getInstance().getSecurity();
        userInterface.main();
    }

    /**
     * Changes the password
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InterruptedException InterruptedException
     * @throws WriterException WriterException
     */
    public static void changePassword() throws InterruptedException, WriterException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String tfaKey = Security.generateTfaKey();
        String password = userInterface.getNewPassword(tfaKey, JFrame.DISPOSE_ON_CLOSE);
        UserFile.create(password, tfaKey);
        UserFile userFile = UserFile.getInstance();
        KeyFile keyFile = KeyFile.getInstance();
        keySecurity = new Security(password, userFile.getSalt(), userFile.getIv());
        KeyFile.create(keyFile.getPassword(), keyFile.getSalt(), keyFile.getIv());
    }

    /**
     * Regenerates the accounts' encryption key
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static void regenerateKey() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException,
            InvalidKeySpecException {
        LinkedList<AccountFile> accountFiles = new LinkedList<>();
        for(String name : AccountFile.getFileList())
            accountFiles.add(new AccountFile(name));
        KeyFile.create();
        accountSecurity = KeyFile.getInstance().getSecurity();
        for(AccountFile accountFile : accountFiles)
            AccountFile.create(accountFile.getName(), accountFile.getUsername(), accountFile.getPassword());
    }
}