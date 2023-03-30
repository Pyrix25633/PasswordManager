package net.segmentation_four.password_manager.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;

public class EncodedInputFile {
    private final FileInputStream stream;
    private final Scanner scanner;

    public EncodedInputFile(String path) throws FileNotFoundException {
        this.stream = new FileInputStream(path);
        this.scanner = new Scanner(this.stream);
    }

    public String next() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new String(Base64.getDecoder().decode(this.scanner.next()));
    }

    public BigInteger nextBigInteger() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return new BigInteger(Base64.getDecoder().decode(this.scanner.next()));
    }

    public byte[] nextBytes() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return Base64.getDecoder().decode(this.scanner.next());
    }

    public void close() throws IOException {
        this.stream.close();
    }
}