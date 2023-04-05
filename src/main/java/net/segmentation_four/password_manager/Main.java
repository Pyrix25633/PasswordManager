package net.segmentation_four.password_manager;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.encryption.*;
import net.segmentation_four.password_manager.ui.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
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
    private static UserInterface userInterface;

    /**
     * Program entrypoint
     * @param args Command-line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InterruptedException, WriterException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        /*Window window = new Window("Dropdown menu test", new Dimension(400, 400));
        ComboBox comboBox = new ComboBox(new String[]{"Ite 1", "Item 2", "It 3"}, new Layout(Layout.Horizontal.CENTER, Layout.Vertical.CENTER),
                new Position(0, 0));
        window.addToPanel(comboBox);
        window.refresh();*/
        userInterface = new ArrayList<>(List.of(args)).contains("nogui") ?
                new CommandLineInterface(System.in, System.out) : new GraphicUserInterface();
        if(!UserFile.exists()) {
            String tfaKey = Security.generateTfaKey();
            UserFile.create(userInterface.getNewPassword(tfaKey), tfaKey);
        }
        UserFile userFile = UserFile.getInstance();
        //keySecurity = new Security(userInterface.getPassword(), userFile.getSalt(), userFile.getIv());
        keySecurity = new Security(userInterface.getPassword(), userFile.getSalt(), userFile.getIv());
        if(!KeyFile.exists())
            KeyFile.create();
        accountSecurity = KeyFile.getInstance().getSecurity();
        if(!AccountFile.exists("Discord"))
            AccountFile.create("Discord", "test", "1234@");
        AccountFile discord = new AccountFile("Discord");
        System.out.println(discord.getUsername() + " " + discord.getPassword());
        userInterface.main();
    }

    public static void changePassword() throws InterruptedException, WriterException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String tfaKey = Security.generateTfaKey();
        String password = userInterface.getNewPassword(tfaKey);
        UserFile.create(password, tfaKey);
        UserFile userFile = UserFile.getInstance();
        KeyFile keyFile = KeyFile.getInstance();
        keySecurity = new Security(password, userFile.getSalt(), userFile.getIv());
        KeyFile.create(keyFile.getPassword(), keyFile.getSalt(), keyFile.getIv());
    }

    public static void regenerateKey() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        LinkedList<AccountFile> accountFiles = new LinkedList<>();
        for(String name : AccountFile.getFileList())
            accountFiles.add(new AccountFile(name));
        KeyFile.create();
        accountSecurity = KeyFile.getInstance().getSecurity();
        for(AccountFile accountFile : accountFiles)
            AccountFile.create(accountFile.getName(), accountFile.getUsername(), accountFile.getPassword());
    }
}