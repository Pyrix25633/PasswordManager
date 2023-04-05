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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AccountFile {
    private static final String DIR = "./.resources/accounts";
    private static final String PATH = DIR + "/$.acc";

    private final String name;
    private final String username;
    private final String password;

    public AccountFile(String name) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.name = name;
        EncryptedInputFile in = new EncryptedInputFile(PATH.replace("$", name), Main.accountSecurity);
        this.username = in.next();
        this.password = in.next();
        in.close();
    }

    public String getName() {
        return name;
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

    public static LinkedList<String> getFileList() {
        List<String> list = Stream.of(Objects.requireNonNull(new File(DIR).listFiles()))
                .filter(file -> !file.isDirectory()).map(File::getName).toList();
        LinkedList<String> files = new LinkedList<>();
        for(String file : list)
            files.add(file.substring(0, file.length() - 4));
        return files;
    }
}