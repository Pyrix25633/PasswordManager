package net.segmentation_four.password_manager.encryption;

import net.segmentation_four.password_manager.Main;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

/**
 * Class for handling the key file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class KeyFile {
    // Constants

    private static final String PATH = "./.resources/PasswordManager.key";

    // Static variables

    private static KeyFile instance = null;

    // Fields

    private final String password;
    private final String salt;
    private final IvParameterSpec iv;
    private final Security security;

    // Constructors

    private KeyFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException,
            InvalidKeySpecException {
        EncryptedInputFile in = new EncryptedInputFile(PATH, Main.keySecurity);
        this.password = in.next();
        this.salt = in.next();
        this.iv = new IvParameterSpec(in.nextBytes());
        this.security = new Security(password, salt, iv);
        in.close();
    }

    // Getters

    /**
     * Gets the field password
     * @return The field password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the field salt
     * @return The field salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Gets the field iv
     * @return The field iv
     */
    public IvParameterSpec getIv() {
        return iv;
    }

    /**
     * Gets the field security
     * @return The field security
     */
    public Security getSecurity() {
        return security;
    }

    // Public static methods

    /**
     * Singleton instance getter
     * @return A KeyFile instance
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static KeyFile getInstance() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException,
            InvalidKeySpecException {
        if(instance == null) instance = new KeyFile();
        return instance;
    }

    /**
     * Tests if the KeyFile exists
     * @return True if it exists
     */
    public static boolean exists() {
        return new File(PATH).exists();
    }

    /**
     * Creates a new KeyFile
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static void create() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH, Main.keySecurity);
        out.println(Security.generatePassword(32));
        out.println(String.valueOf(new Random().nextLong()));
        out.println(Security.generateIv().getIV());
        out.close();
        instance = new KeyFile();
    }

    /**
     * Creates a new KeyFile with specified parameters
     * @param password The password
     * @param salt The salt
     * @param iv The iv
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static void create(String password, String salt, @NotNull IvParameterSpec iv) throws IOException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH, Main.keySecurity);
        out.println(password);
        out.println(salt);
        out.println(iv.getIV());
        out.close();
        instance = new KeyFile();
    }
}