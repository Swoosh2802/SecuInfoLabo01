import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;

import Implementations.HMACMD5;
import Implementations.SHA1Implementation;
import Implementations.ThreeDesImplementation;

import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        String lastMessage= "";
        String AESKEY = "";
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
