package Implementations;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

//https://www.youtube.com/watch?v=1QJ2TMtBUtc&ab_channel=IndianServersUniversity
public class ThreeDesImplementation {
    private KeySpec ks;
    private SecretKeyFactory skf;
    private String myEncryptionKey;
    private Key key;

    public ThreeDesImplementation() throws Exception {
        myEncryptionKey = "cledetest";
        this.skf = SecretKeyFactory.getInstance("DES");
        this.ks =  new DESKeySpec(myEncryptionKey.getBytes());
        this.key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) throws Exception{
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(unencryptedString.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String decrypt(String encryptedString) throws Exception {
        byte[] dataInBytes = Base64.getDecoder().decode(encryptedString);
        Cipher decryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        decryptionCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);
        return new String(decryptedBytes,"UTF-8");
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