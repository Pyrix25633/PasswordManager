package net.segmentation_four.password_manager.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
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

    public String next() {
        return new String(Base64.getDecoder().decode(this.scanner.next()));
    }

    public BigInteger nextBigInteger() {
        return new BigInteger(Base64.getDecoder().decode(this.scanner.next()));
    }

    public byte[] nextBytes() {
        return Base64.getDecoder().decode(this.scanner.next());
    }
    public void close() throws IOException {
        this.stream.close();
    }
}