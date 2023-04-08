package net.segmentation_four.password_manager.encryption;

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
import java.util.Random;

/**
 * Class for handling the user file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class UserFile {
    // Constants

    private static final String PATH = "./.resources/PasswordManager.usr";

    // Static variables

    private static UserFile instance = null;

    // Fields

    private final BigInteger hash;
    private final String tfaKey;
    private final String salt;
    private final IvParameterSpec iv;

    // Constructors

    private UserFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncodedInputFile in = new EncodedInputFile(PATH);
        this.hash = in.nextBigInteger();
        this.tfaKey = in.next();
        this.salt = in.next();
        this.iv = new IvParameterSpec(in.nextBytes());
        in.close();
    }

    // Getters

    /**
     * Gets the hash field
     * @return The hash field
     */
    public BigInteger getHash() {
        return hash;
    }

    /**
     * Gets the tfaKey field
     * @return The tfaKey field
     */
    public String getTfaKey() {
        return tfaKey;
    }

    /**
     * Gets the salt field
     * @return The salt field
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Gets the iv field
     * @return The iv field
     */
    public IvParameterSpec getIv() {
        return iv;
    }

    // Public static methods

    /**
     * Singleton instance getter
     * @return An UserFile instance
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static UserFile getInstance() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(instance == null) instance = new UserFile();
        return instance;
    }

    /**
     * Tests if the UserFile
     * @return True if it exists
     */
    public static boolean exists() {
        return new File(PATH).exists();
    }

    /**
     * Creates an UserFile with the specified parameters
     * @param password The password
     * @param tfaKey The 2FA key
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static void create(String password, String tfaKey) throws IOException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException,
            InvalidKeyException {
        EncodedOutputFile out = new EncodedOutputFile(PATH);
        out.println(Security.SHA512Hash(password));
        out.println(tfaKey);
        out.println(String.valueOf(new Random().nextLong()));
        out.println(Security.generateIv().getIV());
        out.close();
        instance = new UserFile();
    }
}