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

public class EncodedOutputFile {
    private final FileOutputStream fileStream;
    private final PrintStream printStream;

    public EncodedOutputFile(String path) throws FileNotFoundException {
        this.fileStream = new FileOutputStream(path);
        this.printStream = new PrintStream(this.fileStream);
    }

    public void println(String line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line.getBytes()));
    }

    public void println(BigInteger line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line.toByteArray()));
    }

    public void println(byte[] line) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.printStream.println(Base64.getEncoder().encodeToString(line));
    }

    public void close() throws IOException {
        fileStream.flush();
        fileStream.close();
    }
}