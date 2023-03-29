package net.segmentation_four.password_manager.encryption;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;
import java.util.Scanner;

/**
 * Class to handle encryption and decryption
 */
public class Guardian {
    private static final String userFilePath = "./.resources/user.pm";

    private SecretKey key;

    public Guardian() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        BigInteger hash;
        String salt;
        if(new File(userFilePath).exists()) {
            Base64EncodedInputFile in = new Base64EncodedInputFile(userFilePath);
            hash = in.nextBigInteger();
            salt = in.next();
            //TODO: GUI
            System.out.print("Input password: ");
            Scanner console = new Scanner(System.in);
            String password = console.next();
            if(this.SHA512Hash(password).equals(hash)) {
                System.out.println("Password is correct");
                System.out.println("Hash: " + hash);
                System.out.println("Salt: " + salt);
                this.setKey(password, salt);
            }
            else
                System.out.println("Password is not correct");
        }
        else {
            Base64EncodedOutputFile out = new Base64EncodedOutputFile(userFilePath);
            salt = String.valueOf(new Random().nextLong());
            //TODO: GUI
            System.out.print("Input new password: ");
            Scanner console = new Scanner(System.in);
            String password = console.next();
            hash = this.SHA512Hash(password);
            this.setKey(password, salt);
            System.out.println("Hash: " + hash);
            System.out.println("Salt: " + salt);
            out.println(hash);
            out.println(salt);
            out.close();
        }
    }

    public void setKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        this.key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public BigInteger SHA512Hash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest(password.getBytes());
        return new BigInteger(1, hash);
    }
}