package net.segmentation_four.password_manager.encryption;

import java.io.FileNotFoundException;

public class EncryptedInputFile extends EncodedInputFile {

    public EncryptedInputFile(String path) throws FileNotFoundException {
        super(path);
    }


}