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

public class UserFile {
    private static final String PATH = "./.resources/PasswordManager.usr";
    private static UserFile instance = null;

    public static UserFile getInstance() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(instance == null) instance = new UserFile();
        return instance;
    }

    private final BigInteger hash;
    private final String tfaKey;
    private final String salt;
    private final IvParameterSpec iv;

    private UserFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncodedInputFile in = new EncodedInputFile(PATH);
        this.hash = in.nextBigInteger();
        this.tfaKey = in.next();
        this.salt = in.next();
        this.iv = new IvParameterSpec(in.nextBytes());
        in.close();
    }

    public BigInteger getHash() {
        return hash;
    }

    public String getTfaKey() {
        return tfaKey;
    }

    public String getSalt() {
        return salt;
    }

    public IvParameterSpec getIv() {
        return iv;
    }

    public static boolean exists() {
        return new File(PATH).exists();
    }

    public static void create(String password, String tfaKey) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncodedOutputFile out = new EncodedOutputFile(PATH);
        out.println(Security.SHA512Hash(password));
        out.println(tfaKey);
        out.println(String.valueOf(new Random().nextLong()));
        out.println(Security.generateIv().getIV());
        out.close();
        instance = new UserFile();
    }
}