package net.segmentation_four.password_manager.encryption;

import net.segmentation_four.password_manager.Main;

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

public class KeyFile {
    private static final String PATH = "./.resources/PasswordManager.key";
    private static KeyFile instance = null;

    public static KeyFile getInstance() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if(instance == null) instance = new KeyFile();
        return instance;
    }

    private final Security security;

    private KeyFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedInputFile in = new EncryptedInputFile(PATH, Main.keySecurity);
        this.security = new Security(in.next(), in.next(), new IvParameterSpec(in.nextBytes()));
        in.close();
    }

    public Security getSecurity() {
        return security;
    }

    public static boolean exists() {
        return new File(PATH).exists();
    }

    public static void create() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH, Main.keySecurity);
        out.println(Security.generatePassword(32));
        out.println(String.valueOf(new Random().nextLong()));
        out.println(Security.generateIv().getIV());
        out.close();
        instance = new KeyFile();
    }
}