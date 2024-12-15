package dev.dolu.chat_service.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Use 128-bit AES key
        return keyGen.generateKey();
    }

    // Encrypt a message using AES with IV
    public static String encryptMessage(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // Generate a random IV
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());

        // Combine IV and encrypted content
        byte[] ivAndCipherText = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, ivAndCipherText, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(ivAndCipherText);
    }

    // Decrypt a message using AES with IV
    public static String decryptMessage(String encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        byte[] ivAndCipherText = Base64.getDecoder().decode(encryptedMessage);

        // Extract IV
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(ivAndCipherText, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Extract CipherText
        byte[] cipherText = new byte[ivAndCipherText.length - IV_SIZE];
        System.arraycopy(ivAndCipherText, IV_SIZE, cipherText, 0, cipherText.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherText);

        return new String(decryptedBytes);
    }
}
