package net.segmentation_four.password_manager.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

/**
 * Class for reading from an encoded file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class EncodedInputFile {
    // Fields

    private final FileInputStream stream;
    private final Scanner scanner;

    // Constructors

    /**
     * Constructor
     * @param path The file path
     * @throws FileNotFoundException FileNotFoundException
     */
    public EncodedInputFile(String path) throws FileNotFoundException {
        this.stream = new FileInputStream(path);
        this.scanner = new Scanner(this.stream);
    }

    // Public methods

    /**
     * Reads a String
     * @return A String
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public String next() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new String(Base64.getDecoder().decode(this.scanner.next()));
    }

    /**
     * Reads a BigInteger
     * @return A BigInteger
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public BigInteger nextBigInteger() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new BigInteger(Base64.getDecoder().decode(this.scanner.next()));
    }

    /**
     * Reads a byte array
     * @return A byte array
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public byte[] nextBytes() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return Base64.getDecoder().decode(this.scanner.next());
    }

    /**
     * Closes the file
     * @throws IOException IOException
     */
    public void close() throws IOException {
        this.stream.close();
    }
}