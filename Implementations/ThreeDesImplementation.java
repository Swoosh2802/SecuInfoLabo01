package Implementations;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

//https://www.youtube.com/watch?v=1QJ2TMtBUtc&ab_channel=IndianServersUniversity
public class ThreeDesImplementation {
    private static final String UNICODE_Format = "UTF-8";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private String myEncryptionKey;
    Key key;

    public ThreeDesImplementation() throws Exception {
        myEncryptionKey = "cledetest";
        ks =  new DESKeySpec(myEncryptionKey.getBytes(UNICODE_Format));
        skf = SecretKeyFactory.getInstance("DES");
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String textToCrypt = Base64.getEncoder().encodeToString(unencryptedString.getBytes());
            byte[] encryptedText = cipher.doFinal(textToCrypt.getBytes());
            encryptedString = new String(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = cipher.doFinal(encryptedString.getBytes());
            byte[] encValue = Base64.getDecoder().decode(decodedBytes);
            decryptedText = new String(encValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static void main(String args[]) throws Exception {
        ThreeDesImplementation td = new ThreeDesImplementation();
        String target = "Hello World";
        String encrypted = td.encrypt(target);
        String decrypted = td.decrypt(encrypted);
        System.out.println("String To Encrypt:" + target);
        System.out.println("Encrypted String:" + encrypted);
        System.out.println("Decrypted String:" + decrypted);
    }
}