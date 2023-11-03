import java.net.*;
import java.nio.charset.StandardCharsets;
import Implementations.HMAC;

import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(4200)) {
            while (true) {
                Socket client = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));

                String messageWithHMAC = fromClient.readLine();
                String receivedMessage = messageWithHMAC.substring(0, messageWithHMAC.length() - 32); // Remove HMAC
                String receivedHMAC = messageWithHMAC.substring(messageWithHMAC.length() - 32); // Extract HMAC

                HMAC hmac = new HMAC();
                String calculatedHMAC = hmac.calculateHMAC(receivedMessage);
    
                if (receivedHMAC.equals(calculatedHMAC)) {
                    System.out.println("Message authentique : " + receivedMessage);
                } else {
                    System.out.println("Message non authentique !");
                }
            }
        } catch (IOException exception) {
            System.out.println("Erreur serveur: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
