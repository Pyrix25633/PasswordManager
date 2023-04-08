package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Interface for handling user inteface
 */
public interface UserInterface {
    // Public methods

    /**
     * Gets the password
     * @return The password
     * @throws InterruptedException InterruptedException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws IOException IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeyException InvalidKeyException
     */
    String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    /**
     * Gets the new password
     * @param tfaKey The 2FA key
     * @param defaultCloseOperation The default close operation of the GUI window
     * @return The new password
     * @throws InterruptedException InterruptedException
     * @throws WriterException WriterException
     * @throws IOException IOException
     */
    String getNewPassword(String tfaKey, int defaultCloseOperation) throws InterruptedException, WriterException,
            IOException;

    /**
     * Main interface
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws BadPaddingException BadPaddingException
     * @throws InvalidKeySpecException InvalidKeySpecException
     * @throws InvalidKeyException InvalidKeyException
     * @throws InterruptedException InterruptedException
     * @throws WriterException WriterException
     */
    void main() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException,
            InvalidKeyException, InterruptedException, WriterException;
}