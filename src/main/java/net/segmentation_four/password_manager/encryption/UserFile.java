package net.segmentation_four.password_manager.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UserFile {
    private static final String path = "./.resources/PasswordManager.usr";
    private static UserFile instance = null;

    public static UserFile getInstance() throws FileNotFoundException {
        if(instance == null) instance = new UserFile();
        return instance;
    }

    private BigInteger hash;
    private String salt;
    private IvParameterSpec iv;

    public UserFile() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncodedInputFile in = new EncodedInputFile(path);
        this.hash = in.nextBigInteger();
        this.salt = in.next();
        this.iv = new IvParameterSpec(in.nextBytes());
        in.close();
    }

    public BigInteger getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public IvParameterSpec getIv() {
        return iv;
    }

    public static boolean exists() {
        return new File(path).exists();
    }

    public static void create(String password) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncodedOutputFile out = new EncodedOutputFile(path);
        out.println(Security.SHA512Hash(password));
        out.println(String.valueOf(new Random().nextLong()));
        out.println(Security.generateIv().getIV());
        out.close();
    }
}