package net.segmentation_four.password_manager.encryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Random;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

/**
 * Class to handle encryption and decryption
 */
public class Security {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static ArrayList<Character> validPasswordChars = null;
    public static final String TFA_QR_PATH = "./.resources/PasswordManager.tfa";

    private final SecretKey key;
    private final IvParameterSpec iv;

    public Security(String password, String salt, IvParameterSpec iv) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        this.key = keyFromPassword(password, salt);
        this.iv = iv;
    }

    public static SecretKey keyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static BigInteger SHA512Hash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest(password.getBytes());
        return new BigInteger(1, hash);
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public byte[] encrypt(byte[] input)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        return cipher.doFinal(input);
    }

    public byte[] decrypt(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        return cipher.doFinal(cipherText);
    }

    private static ArrayList<Character> getValidPasswordChars() {
        if(validPasswordChars == null) {
            validPasswordChars = new ArrayList<>();
            for(char c = 'A'; c <= 'Z'; c++)
                validPasswordChars.add(c);
            for(char c = 'a'; c <= 'z'; c++)
                validPasswordChars.add(c);
            for(char c = '0'; c <= '9'; c++)
                validPasswordChars.add(c);
            validPasswordChars.add('+');
            validPasswordChars.add('-');
            validPasswordChars.add('*');
            validPasswordChars.add('/');
            validPasswordChars.add('=');
            validPasswordChars.add('_');
            validPasswordChars.add('!');
            validPasswordChars.add('?');
            validPasswordChars.add('.');
            validPasswordChars.add(':');
            validPasswordChars.add('#');
            validPasswordChars.add('@');
            validPasswordChars.add('$');
        }
        return validPasswordChars;
    }

    public static boolean isValidPassword(String password) {
        ArrayList<Character> validChars = getValidPasswordChars();
        int uppercaseLetters = 0, symbols = 0, numbers = 0;
        for(char c : password.toCharArray()) {
            if(!validChars.contains(c)) return false;
            if(Character.isDigit(c)) numbers++;
            else if(Character.isUpperCase(c)) uppercaseLetters++;
            else if(!Character.isAlphabetic(c)) symbols++;
        }
        return uppercaseLetters >= 1 && symbols >= 1 && numbers >= 2 && password.length() >= 8;
    }

    public static String passwordFeedback(String password) {
        ArrayList<Character> validChars = getValidPasswordChars();
        int uppercaseLetters = 0, symbols = 0, numbers = 0;
        for(char c : password.toCharArray()) {
            if(!validChars.contains(c)) return c + " is not allowed!";
            if(Character.isDigit(c)) numbers++;
            else if(Character.isUpperCase(c)) uppercaseLetters++;
            else if(!Character.isAlphabetic(c)) symbols++;
        }
        if(uppercaseLetters < 1) return uppercaseLetters + "/1+ uppercase letters!";
        if(symbols < 1) return symbols + "/1+ symbols!";
        if(numbers < 2) return numbers + "/2+ numbers!";
        if(password.length() < 8) return password.length() + "/8+ characters!";
        return "Valid password";
    }

    public static String generatePassword(int length) {
        ArrayList<Character> validChars = getValidPasswordChars();
        if(length < 8) length = 8;
        else if(length > 32) length = 32;
        StringBuilder password = new StringBuilder();
        for(int i = 0; i < length; i++) {
            password.append(validChars.get(new Random().nextInt(validChars.size())));
        }
        return isValidPassword(password.toString()) ? password.toString() : generatePassword(length);
    }

    public static String generateTfaKey() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        return new Base32().encodeToString(bytes);
    }

    public static String getTOTPCode(String tfaKey) {
        byte[] bytes = new Base32().decode(tfaKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static void createGoogleAuthenticatorQRCode(String tfaKey) throws WriterException {
        String pm = "PasswordManager";
        String barCode = "otpauth://totp/"
                + URLEncoder.encode(pm + ":" + pm, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret=" + URLEncoder.encode(tfaKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer=" + URLEncoder.encode(pm, StandardCharsets.UTF_8).replace("+", "%20");
        BitMatrix matrix = new MultiFormatWriter().encode(barCode, BarcodeFormat.QR_CODE,
                640, 640);
        try (FileOutputStream out = new FileOutputStream(TFA_QR_PATH)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}