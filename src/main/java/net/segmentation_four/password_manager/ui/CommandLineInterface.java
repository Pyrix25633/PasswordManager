package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;
import net.segmentation_four.password_manager.Main;
import net.segmentation_four.password_manager.encryption.AccountFile;
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
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class for handling command line interface
 * @author Segmentation Four
 * @version 1.0.0
 */
public class CommandLineInterface implements UserInterface {
    //Fields

    private final Scanner in;
    private final PrintStream out;

    //Constructors

    /**
     * Constructor
     * @param in The input stream
     * @param out The print stream
     */
    public CommandLineInterface(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    // Public methods

    @Override
    public String getPassword() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UserFile userFile = UserFile.getInstance();
        out.print("Password: ");
        String password = in.nextLine();
        while(!Security.SHA512Hash(password).equals(userFile.getHash())) {
            out.println("Wrong Password!");
            out.print("Password: ");
            password = in.nextLine();
        }
        out.print("2FA Code: ");
        String tfaCode = in.nextLine();
        while(!Security.getTOTPCode(userFile.getTfaKey()).equals(tfaCode.replace(" ", ""))) {
            out.println("Wrong 2FA Code!");
            out.print("2FA Code: ");
            tfaCode = in.nextLine();
        }
        return password;
    }

    @Override
    public String getNewPassword(String tfaKey, int defaultCloseOperation) {
        out.print("New Password: ");
        String password = in.nextLine();
        String message = Security.passwordFeedback(password);
        while(message.contains("!")) {
            out.println(message);
            out.print("New Password: ");
            password = in.nextLine();
            message = Security.passwordFeedback(password);
        }
        System.out.println("2FactorAuthentication Key: " + tfaKey);
        return password;
    }

    @Override
    public void main() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException, InterruptedException, WriterException {
        boolean exit;
        do {
            System.out.println("Menu");
            System.out.println(" 1. Change Password");
            System.out.println(" 2. Regenerate Key");
            System.out.println(" 3. Create/Modify Account");
            System.out.println(" 4. Generate Random Password");
            System.out.println(" 5. Show Account");
            System.out.println(" 6. Delete Account");
            System.out.print("Choice: ");
            switch(Integer.parseInt(in.nextLine())) {
                case 1 -> Main.changePassword();
                case 2 -> Main.regenerateKey();
                case 3 -> {
                    System.out.print("Account Name: ");
                    String name = in.nextLine();
                    System.out.print("Username/E-mail: ");
                    String username = in.nextLine();
                    System.out.print("Password: ");
                    AccountFile.create(name, username, in.nextLine());
                }
                case 4 -> {
                    System.out.print("Random Password Length: ");
                    System.out.println(Security.generatePassword(Integer.parseInt(in.nextLine())));
                }
                case 5 -> {
                    AccountFile account = new AccountFile(getAccountName());
                    System.out.println("Username/E-mail: " + account.getUsername());
                    System.out.println("Password: " + account.getPassword());
                }
                case 6 -> AccountFile.delete(getAccountName());
                default -> System.out.println("No Such Option!");
            }
            System.out.print("Exit? (y/N): ");
            String wantsToExit = in.nextLine();
            exit = wantsToExit.equalsIgnoreCase("Y") || wantsToExit.equalsIgnoreCase("Yes");
        } while(!exit);
    }

    // Private methods

    private String getAccountName() {
        LinkedList<String> matches;
        do {
            System.out.print("Account Name: ");
            matches = new LinkedList<>();
            String input = in.nextLine();
            for(String name : AccountFile.getFileList())
                if(name.contains(input))
                    matches.add(name);
            if(matches.size() != 1)
                System.out.println(matches.size() + " Matches");
            for(String match : matches)
                System.out.println(" - " + match);
        } while(matches.size() != 1);
        return matches.get(0);
    }
}