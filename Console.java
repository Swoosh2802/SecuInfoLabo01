import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Implementations.AESImplementation;
import Implementations.HMACMD5;
import Implementations.SHA1Implementation;
import Implementations.ThreeDesImplementation;

public class Console {
    java.io.Console console = System.console();
    ThreeDesImplementation tripleDes;
    AESImplementation aesAlgo;
    SHA1Implementation sha1Implementation;
    HMACMD5 hmacmd5 = new HMACMD5();

    PublicKey encryptKeyPublicKey;
    KeyPairGenerator encryptKeyPairGenerator;
    PrivateKey encryptPrivateKey;
    SecretKey aesEncryptKey; 
    PublicKey receivedPublicKey;
    byte[] iv =null;
    Cipher encryptionCipher;


    public Console() throws Exception {
        tripleDes = new ThreeDesImplementation();
        aesAlgo = new AESImplementation();
        sha1Implementation = new SHA1Implementation();
        hmacmd5 = new HMACMD5();


        encryptKeyPairGenerator = KeyPairGenerator.getInstance("DH");
        encryptKeyPairGenerator.initialize(2048);
        KeyPair keyPair = encryptKeyPairGenerator.generateKeyPair();
        PrivateKey encryptPrivateKey = keyPair.getPrivate();
        PublicKey encryptPublicKey = keyPair.getPublic();

        try (Socket server = new Socket("localhost", 4201)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            objectOutputStream.writeObject(encryptPublicKey);
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (ServerSocket  serverSocket = new ServerSocket(4202)) {
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (receivedPublicKey == null || iv == null) {
                Object obj = objectInputStream.readObject();
                if (obj instanceof PublicKey) {
                    receivedPublicKey = (PublicKey) obj;
                } else if (obj instanceof byte[]) {
                    iv = (byte[]) obj;
                }
            }
            objectInputStream.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(encryptPrivateKey);
        keyAgreement.doPhase(receivedPublicKey, true);
        
        aesEncryptKey = new SecretKeySpec(keyAgreement.generateSecret(),0, 16, "AES");

        encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, this.aesEncryptKey, new IvParameterSpec(iv));
        
        while(true){
            String choice = console.readLine("Quel algorithme utilisez-vous?\n 1) 3DES \n 2) AES \n");
            choosenAlgorithm(choice);
        }
    }

    public void choosenAlgorithm(String algorithm) throws Exception{
        HMACMD5 hmac;
        String calculatedHMAC;

        switch (algorithm) {
                case "1":
                    String toCypher = console.readLine("Entrez le texte à chiffrer\n");

                    hmac = new HMACMD5();
                    calculatedHMAC = hmac.calculateHMAC(toCypher); //Ajout de l'authentification HMAC-MD5
                    toCypher = toCypher+calculatedHMAC; //Ajout de l'authentification HMAC-MD5
                    String ciphered = tripleDes.encrypt(toCypher);
                    Client.sendMessage("3DE:"+ciphered);
                    Client.sendMessage(sha1Implementation.sha1Hash("3DE:"+ciphered)); //Envoi du Hash SHA-1
                case "2":
                    toCypher = console.readLine("Entrez le texte à chiffrer\n");
                    byte[] encryptedBytes = encryptionCipher.doFinal(toCypher.getBytes("UTF-8"));
                    /*
                    hmac = new HMACMD5();
                    calculatedHMAC = hmac.calculateHMAC(toCypher); //Ajout de l'authentification HMAC-MD5
                    toCypher = toCypher+calculatedHMAC; //Ajout de l'authentification HMAC-MD5
                    */
                    Client.sendMessage("AES:"+Base64.getEncoder().encodeToString(encryptedBytes));
                    Client.sendMessage(sha1Implementation.sha1Hash("AES:"+Base64.getEncoder().encodeToString(encryptedBytes)));
                default:
                    choosenAlgorithm(console.readLine("Quel algorithme utilisez-vous?\n 1) 3DES \n 2) AES \n"));
            }
    }
}
