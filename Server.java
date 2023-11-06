import java.net.*;
import java.nio.charset.StandardCharsets;
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

import Implementations.HMACMD5;
import Implementations.SHA1Implementation;
import Implementations.ThreeDesImplementation;

import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        String lastMessage= "";

        PublicKey decryptPublicKey;
        KeyPairGenerator decryptKeyPairGenerator;
        PrivateKey decryptPrivateKey;
        SecretKey aesDecryptKey = null; 
        byte[] iv = new byte[16];

        
        decryptKeyPairGenerator = KeyPairGenerator.getInstance("DH");
        decryptKeyPairGenerator.initialize(2048);
        KeyPair keyPair2 = decryptKeyPairGenerator.generateKeyPair();
        decryptPrivateKey = keyPair2.getPrivate();
        decryptPublicKey = keyPair2.getPublic();

        try (ServerSocket  serverSocket = new ServerSocket(4201)) {
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            PublicKey receivedPublicKey = (PublicKey) objectInputStream.readObject();
            objectInputStream.close();
            socket.close();

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(decryptPrivateKey);
            keyAgreement.doPhase(receivedPublicKey, true);
            
            aesDecryptKey = new SecretKeySpec(keyAgreement.generateSecret(),0, 16, "AES");
        } catch (IOException exception) {
            System.out.println("Erreur socket diffie: " + exception.getMessage());
            exception.printStackTrace();
        }

        try (Socket server = new Socket("localhost", 4202)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            objectOutputStream.writeObject(decryptPublicKey);
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(4200)) {
            while (true) {
                Socket client = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                String receivedMessage = fromClient.readLine();
                if(receivedMessage.contains(":")){
                    lastMessage = receivedMessage;
                    if(receivedMessage.substring(0,receivedMessage.indexOf(":")).equals("3DE")){
                        String message = receivedMessage.substring(receivedMessage.indexOf(":")+1);
                        ThreeDesImplementation threeDesImplementation = new ThreeDesImplementation();
                        String uncipheredMessage = threeDesImplementation.decrypt(message);

                        String messageWithNoHMAC = uncipheredMessage.substring(0,uncipheredMessage.length() - 32);
                        String receivedHMAC = uncipheredMessage.substring(uncipheredMessage.length() - 32); // Extract HMAC
                        HMACMD5 hmac = new HMACMD5();
                        String calculatedHMAC = hmac.calculateHMAC(messageWithNoHMAC);
        
                        if (receivedHMAC.equals(calculatedHMAC)) {
                            System.out.println("Expéditeur authentifié par HMAC-MD5 : " + messageWithNoHMAC);
                        } else {
                            System.out.println("Expéditeur non authentifié !");
                        }
                    } else if(receivedMessage.substring(0,receivedMessage.indexOf(":")).equals("AES")){
                        String message = receivedMessage.substring(receivedMessage.indexOf(":")+1);
                        byte[] dataInBytes = Base64.getDecoder().decode(message);
                        Cipher decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        decryptionCipher.init(Cipher.DECRYPT_MODE, aesDecryptKey, new IvParameterSpec(iv));
                        byte[] decryptedBytes = decryptionCipher.doFinal(dataInBytes);
                        System.out.println(new String(decryptedBytes,"UTF-8"));
                    }
                }else{
                    SHA1Implementation sha1Implementation = new SHA1Implementation();
                    System.out.println(lastMessage);
                    System.out.println(receivedMessage);
                    if(sha1Implementation.sha1Hash(lastMessage).equals(receivedMessage)){
                        System.out.println("Le message est authentique");
                    }else{
                        System.out.println("Le message a été modifié!");
                    }
                }
            }  
        } catch (IOException exception) {
            System.out.println("Erreur serveur: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
