package ca.uoit.flip_flop.flipflop;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHandler {
    /**
     * securePassword
     * Generates a secured password with a base password, a generated salt, and PBKDF2
     * implementation of SHA-512
     * @param password
     * @param salt
     * @return
     */
    public static String securePassword(char[] password, byte[] salt){
        String generatedPassword = null;
        final int iterations = 500;
        final int keyLength = 512;

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(keySpec);
            byte[] result = key.getEncoded();
            generatedPassword = bytesToHexString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * bytesToHexString
     * Converts a given byte array to a hexadecimal string, used to decode generated
     * keys and password salts
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        BigInteger bigInt = new BigInteger(1, bytes);
        String hex = bigInt.toString(16);
        int padding = (bytes.length * 2) - hex.length();
        if (padding != 0) {
            return String.format("%0"  +padding + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * hexStringToBytes
     * Converts a given hexadecimal string to a byte array, used to convert a retrieved salt
     * back to byte-form so it can be used in password comparison
     * @param input
     * @return
     */
    public static byte[] hexStringToBytes(String input) {
        byte[] bytes = new byte[input.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            bytes[i] = (byte)Integer.parseInt(input.substring(index, index + 2), 16);
        }
        return bytes;
    }

    /**
     * generateSalt
     * Generates a password salt using SHA-1 pseudo-random number generator
     * @return
     */
    public static byte[] generateSalt() {
        final int saltLength = 16;
        byte[] salt = new byte[saltLength];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(salt);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return salt;
    }


}
