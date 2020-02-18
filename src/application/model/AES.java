package application.model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {
    public static String encode(String secretMessage, String secretKey) {
        Key aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(secretMessage.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return null;
        }
    }

    public static String decode(String strToDecrypt, String secret) throws Exception {
        Key aesKey = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    public static String getKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            byte[] binary = secretKey.getEncoded();
            String key = String.format("%032X", new BigInteger(+1, binary));
            return key.substring(0, Math.min(key.length(), 16));
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
