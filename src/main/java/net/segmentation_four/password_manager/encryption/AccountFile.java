package net.segmentation_four.password_manager.encryption;

import net.segmentation_four.password_manager.Main;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that reads and writes account files
 * @author Segmentation Four
 * @version 1.0.0
 */
public class AccountFile {
    // Constants

    private static final String DIR = "./.resources/accounts";
    private static final String PATH = DIR + "/$.acc";

    // Fields

    private final String name;
    private final String username;
    private final String password;

    // Constructors

    /**
     * Constructor
     * @param name The file name, without the extension
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public AccountFile(String name) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.name = name;
        EncryptedInputFile in = new EncryptedInputFile(PATH.replace("$", name), Main.accountSecurity);
        this.username = in.next();
        this.password = in.next();
        in.close();
    }

    // Getters

    /**
     * Gets the field name
     * @return The field name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the field username
     * @return The field username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the field password
     * @return The field password
     */
    public String getPassword() {
        return password;
    }

    // Public static methods

    /**
     * Creates an AccountFile
     * @param name The file name, without the extension
     * @param username The username
     * @param password The password
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static void create(String name, String username, String password) throws IOException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH.replace("$", name), Main.accountSecurity);
        out.println(username);
        out.println(password);
        out.close();
    }

    /**
     * Gets a list of all AccountFiles
     * @return A list of the names of all the AccountFiles
     */
    public static @NotNull LinkedList<String> getFileList() {
        List<String> list = Stream.of(Objects.requireNonNull(new File(DIR).listFiles()))
                .filter(file -> !file.isDirectory()).map(File::getName).toList();
        LinkedList<String> files = new LinkedList<>();
        for(String file : list)
            files.add(file.substring(0, file.length() - 4));
        return files;
    }

    /**
     * Removes an AccountFile
     * @param name The file name, without the extension
     */
    public static void delete(String name) {
        new File(PATH.replace("$", name)).delete();
    }
}