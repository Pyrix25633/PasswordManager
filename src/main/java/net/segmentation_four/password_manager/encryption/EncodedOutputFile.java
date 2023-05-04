package net.segmentation_four.password_manager.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Class that writes to an encoded file
 * @author Segmentation Four
 * @version 1.0.0
 */
public class EncodedOutputFile {
    // Fields

    private final FileOutputStream fileStream;
    private final PrintStream printStream;

    // Constructors

    /**
     * Constructor
     * @param path The file path
     * @throws FileNotFoundException FileNotFoundException
     */
    public EncodedOutputFile(String path) throws FileNotFoundException {
        this.fileStream = new FileOutputStream(path);
        this.printStream = new PrintStream(this.fileStream);
    }

    // Public methods

    /**
     * Writes a String
     * @param line A String
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public void println(String line) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line.getBytes()));
    }

    /**
     * Writes a BigInteger
     * @param line A BigInteger
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public void println(BigInteger line) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line.toByteArray()));
    }

    /**
     * Writes a byte array
     * @param line A byte array
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    public void println(byte[] line) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line));
    }

    /**
     * Closes the file
     * @throws IOException IOException
     */
    public void close() throws IOException {
        fileStream.flush();
        fileStream.close();
    }
}