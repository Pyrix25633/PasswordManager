package net.segmentation_four.password_manager.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Scanner;

public class Base64EncodedInputFile {
    private final Scanner scanner;

    public Base64EncodedInputFile(String path) throws FileNotFoundException {
        this.scanner = new Scanner(new FileInputStream(path));
    }

    public String next() {
        return new String(Base64.getDecoder().decode(this.scanner.next()));
    }

    public BigInteger nextBigInteger() {
        return new BigInteger(Base64.getDecoder().decode(this.scanner.next()));
    }
}