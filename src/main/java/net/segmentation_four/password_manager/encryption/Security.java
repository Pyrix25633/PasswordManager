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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Class that handles encryption and decryption
 * @author Segmenatation Four
 * @version 1.1.0
 */
public class Security {
    // Constants

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Constants that stores the path to the 2FA QR
     */
    public static final String TFA_QR_PATH = "./.resources/PasswordManager.tfa";

    // Static variables

    private static ArrayList<Character> validPasswordChars = null;

    // Fields

    private final SecretKey key;
    private final IvParameterSpec iv;

    // Constructors

    /**
     * Constructor
     * @param password The password
     * @param salt The salt
     * @param iv The iv
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public Security(String password, String salt, IvParameterSpec iv) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        this.key = keyFromPassword(password, salt);
        this.iv = iv;
    }

    // Public methods

    /**
     * Encrypts a byte array
     * @param input The byte array to encrypt
     * @return The encrypted byte array
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public byte[] encrypt(byte[] input)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        return cipher.doFinal(input);
    }

    /**
     * Decrypts a byte array
     * @param cipherText The byte array to decrypt
     * @return The decrypted byte array
     * @throws NoSuchPaddingException NoSuchPaddingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    public byte[] decrypt(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        return cipher.doFinal(cipherText);
    }

    // Public static methods

    /**
     * Creates a SecretKey from a String
     * @param password The password
     * @param salt The salt
     * @return The SecretKey
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException InvalidKeySpecException
     */
    public static @NotNull SecretKey keyFromPassword(@NotNull String password, @NotNull String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Hashes a String in to a BigInteger with SHA-512
     * @param password The String
     * @return The hash
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static @NotNull BigInteger SHA512Hash(@NotNull String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest(password.getBytes());
        return new BigInteger(1, hash);
    }

    /**
     * Hashes a String in to a BigInteger with the much more secure SHA3-512
     * @param password The String
     * @return The hash
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static @NotNull BigInteger SHA3512Hash(@NotNull String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest(password.getBytes());
        return new BigInteger(1, hash);
    }

    /**
     * Generates an iv
     * @return An iv
     */
    @Contract(" -> new")
    public static @NotNull IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Tests if a password is valid
     * @param password The password
     * @return True if the password is valid
     */
    public static boolean isValidPassword(@NotNull String password) {
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

    /**
     * Gives a String feedback for the user on a specified password
     * @param password The password
     * @return A String feedback
     */
    public static @NotNull String passwordFeedback(@NotNull String password) {
        ArrayList<Character> validChars = getValidPasswordChars();
        int uppercaseLetters = 0, symbols = 0, numbers = 0;
        for(char c : password.toCharArray()) {
            if(!validChars.contains(c)) return "Not Valid Character: '" + c + "'!";
            if(Character.isDigit(c)) numbers++;
            else if(Character.isUpperCase(c)) uppercaseLetters++;
            else if(!Character.isAlphabetic(c)) symbols++;
        }
        if(uppercaseLetters < 1) return uppercaseLetters + "/1+ Uppercase Letters!";
        if(symbols < 1) return symbols + "/1+ Symbols!";
        if(numbers < 2) return numbers + "/2+ Numbers!";
        if(password.length() < 8) return password.length() + "/8+ Characters!";
        return "Valid Password";
    }

    /**
     * Generates a valid password
     * @param length The length of the password
     *               <pre>{@code if(length < 8) length = 8; else if(length > 32) length = 32;}</pre>
     * @return A valid password of the specified length
     */
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

    /**
     * Generates a 2FA key
     * @return A 2FA key
     */
    public static String generateTfaKey() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        return new Base32().encodeToString(bytes);
    }

    /**
     * Gets the TOTP code with a 2FA key
     * @param tfaKey The 2FA key
     * @return The TOTP
     */
    public static String getTOTPCode(String tfaKey) {
        byte[] bytes = new Base32().decode(tfaKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    /**
     * Creates an Authenticator QR code and writes it to <pre>{@code Security.TFA_QR_PATH}</pre>
     * @param tfaKey The 2FA key
     * @throws WriterException WriterException
     */
    public static void createAuthenticatorQRCode(String tfaKey) throws WriterException {
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

    // Private static methods

    /**
     * Gets an ArrayList of valid password chars
     * @return The ArrayList of valid password chars
     */
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
}