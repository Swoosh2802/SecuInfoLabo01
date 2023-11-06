package Implementations;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.KeyAgreement;

//https://www.section.io/engineering-education/implementing-aes-encryption-and-decryption-in-java/

public class AESImplementation {
    private SecretKey encrypt_Key;
    private PublicKey encryptPublicKey;
    private PublicKey decryptPublicKey;
    KeyPairGenerator encryptKeyPairGenerator;
    KeyPairGenerator decryptKeyPairGenerator;
    byte[] iv = new byte[16];

    public AESImplementation() throws InvalidKeyException, IllegalStateException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        
        encryptKeyPairGenerator = KeyPairGenerator.getInstance("DH");
        encryptKeyPairGenerator.initialize(2048);
        KeyPair keyPair = encryptKeyPairGenerator.generateKeyPair();
        PrivateKey encryptPrivateKey = keyPair.getPrivate();
        this.encryptPublicKey = keyPair.getPublic();

        decryptKeyPairGenerator = KeyPairGenerator.getInstance("DH");
        decryptKeyPairGenerator.initialize(2048);
        KeyPair keyPair2 = decryptKeyPairGenerator.generateKeyPair();
        PrivateKey decryptPrivateKey = keyPair2.getPrivate();
        this.decryptPublicKey = keyPair2.getPublic();

        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(encryptPrivateKey);
        keyAgreement.doPhase(decryptPublicKey, true);

        KeyAgreement keyAgreement2 = KeyAgreement.getInstance("DH");
        keyAgreement2.init(decryptPrivateKey);
        keyAgreement2.doPhase(encryptPublicKey, true);
        
        this.encrypt_Key = new SecretKeySpec(keyAgreement.generateSecret(),0, 16, "AES");
    }

    public String encrypt(String text) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        try {
            Cipher encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, this.encrypt_Key, new IvParameterSpec(iv));
            byte[] encryptedBytes = encryptionCipher.doFinal(text.getBytes("UTF-8"));
            System.out.println(Base64.getEncoder().encodeToString(encryptedBytes));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String decrypt(String encryptedData) throws Exception {
        byte[] dataInBytes = Base64.getDecoder().decode(encryptedData);
        Cipher decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptionCipher.init(Cipher.DECRYPT_MODE, this.encrypt_Key, new IvParameterSpec(iv));
        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);
        return new String(decryptedBytes,"UTF-8");
    }

    public static void main(String args[]) throws Exception {
        AESImplementation aes = new AESImplementation();
        String target = "Hello World";
        String encrypted = aes.encrypt(target);
        String decrypted = aes.decrypt(encrypted);
        System.out.println("String To Encrypt:" + target);
        System.out.println("Encrypted String:" + encrypted);
        System.out.println("Decrypted String:" + decrypted);
    }
}
