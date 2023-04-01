package net.segmentation_four.password_manager.ui;

import com.google.zxing.WriterException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserInterface {
    String getPassword() throws InterruptedException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    String getNewPassword(String tfaKey) throws InterruptedException, WriterException;

    void tfAuthenticate() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InterruptedException;
}