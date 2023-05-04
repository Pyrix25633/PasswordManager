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
 * Class that reads from an encrypted file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class EncryptedInputFile extends EncodedInputFile {
    // Fields

    private final Security security;

    // Constructors

    /**
     * Constructor
     * @param path The file path
     * @param security The Security instance with the key used for encrypting
     * @throws FileNotFoundException FileNotFoundException
     */
    public EncryptedInputFile(String path, Security security) throws FileNotFoundException {
        super(path);
        this.security = security;
    }

    // Public methods

    @Override
    public String next() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new String(security.decrypt(super.nextBytes()));
    }

    @Override
    public BigInteger nextBigInteger() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new BigInteger(security.decrypt(super.nextBytes()));
    }

    @Override
    public byte[] nextBytes() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return security.decrypt(super.nextBytes());
    }
}