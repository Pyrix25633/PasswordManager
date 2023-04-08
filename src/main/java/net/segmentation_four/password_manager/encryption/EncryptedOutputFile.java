package net.segmentation_four.password_manager.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Class fo writing to an encrypted file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class EncryptedOutputFile extends EncodedOutputFile {
    // Fields

    private final Security security;

    // Constructors

    /**
     * Constructor
     * @param path The file path
     * @param security The Security instance with the key used for decryption
     * @throws FileNotFoundException FileNotFoundException
     */
    public EncryptedOutputFile(String path, Security security) throws FileNotFoundException {
        super(path);
        this.security = security;
    }

    // Public methods

    @Override
    public void println(String line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        super.println(security.encrypt(line.getBytes()));
    }

    @Override
    public void println(BigInteger line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        super.println(security.encrypt(line.toByteArray()));
    }

    @Override
    public void println(byte[] line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        super.println(security.encrypt(line));
    }
}