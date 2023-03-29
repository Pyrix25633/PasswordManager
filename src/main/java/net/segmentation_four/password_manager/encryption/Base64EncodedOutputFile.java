package net.segmentation_four.password_manager.encryption;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Base64;

public class Base64EncodedOutputFile {
    private final FileOutputStream fileStream;
    private final PrintStream printStream;

    public Base64EncodedOutputFile(String path) throws FileNotFoundException {
        this.fileStream = new FileOutputStream(path);
        this.printStream = new PrintStream(this.fileStream);
    }

    public void println(String line) {
        this.printStream.println(Base64.getEncoder().encodeToString(line.getBytes()));
    }

    public void println(BigInteger line) {
        this.printStream.println(Base64.getEncoder().encodeToString(line.toByteArray()));
    }

    public void close() throws IOException {
        fileStream.flush();
        fileStream.close();
    }
}