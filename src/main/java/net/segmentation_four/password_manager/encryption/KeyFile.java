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

    private final String password;
    private final String salt;
    private final IvParameterSpec iv;
    private final Security security;

    private KeyFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedInputFile in = new EncryptedInputFile(PATH, Main.keySecurity);
        this.password = in.next();
        this.salt = in.next();
        this.iv = new IvParameterSpec(in.nextBytes());
        this.security = new Security(password, salt, iv);
        in.close();
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public IvParameterSpec getIv() {
        return iv;
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

    public static void create(String password, String salt, IvParameterSpec iv) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH, Main.keySecurity);
        out.println(password);
        out.println(salt);
        out.println(iv.getIV());
        out.close();
        instance = new KeyFile();
    }
}