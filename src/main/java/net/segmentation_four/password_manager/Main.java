package net.segmentation_four.password_manager;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.encryption.*;
import net.segmentation_four.password_manager.ui.CommandLineInterface;
import net.segmentation_four.password_manager.ui.GraphicUserInterface;
import net.segmentation_four.password_manager.ui.UserInterface;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
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
    public static Security keySecurity;
    public static Security accountSecurity;

    /**
     * Program entrypoint
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InterruptedException, WriterException {
        UserInterface userInterface = new ArrayList<>(List.of(args)).contains("nogui") ?
                new CommandLineInterface(System.in, System.out) : new GraphicUserInterface();
        if(!UserFile.exists()) {
            String tfaKey = Security.generateTfaKey();
            UserFile.create(userInterface.getNewPassword(tfaKey), tfaKey);
        }
        UserFile userFile = UserFile.getInstance();
        keySecurity = new Security(userInterface.getPassword(), userFile.getSalt(), userFile.getIv());
        if(!KeyFile.exists())
            KeyFile.create();
        accountSecurity = KeyFile.getInstance().getSecurity();
        if(!AccountFile.exists("Discord"))
            AccountFile.create("Discord", "discord@test.com", "#StrongestPasswordEver12");
        AccountFile discord = new AccountFile("Discord");
        System.out.println(discord.getUsername() + " " + discord.getPassword());
    }
}