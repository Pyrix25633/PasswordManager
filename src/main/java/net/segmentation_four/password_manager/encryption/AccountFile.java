package net.segmentation_four.password_manager.encryption;

import net.segmentation_four.password_manager.Main;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AccountFile {
    private static final String PATH = "./.resources/accounts/$.acc";

    private final String username;
    private final String password;

    public AccountFile(String name) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncryptedInputFile in = new EncryptedInputFile(PATH.replace("$", name), Main.accountSecurity);
        this.username = in.next();
        this.password = in.next();
        in.close();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static boolean exists(String name) {
        return new File(PATH.replace("$", name)).exists();
    }

    public static void create(String name, String username, String password) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        EncryptedOutputFile out = new EncryptedOutputFile(PATH.replace("$", name), Main.accountSecurity);
        out.println(username);
        out.println(password);
        out.close();
    }
}