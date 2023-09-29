import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        sendMessage();
    }

    public static void sendMessage() {
        String message = "test";
        try (Socket server = new Socket("localhost", 4200)) {
            PrintWriter toServer = new PrintWriter(
                    new OutputStreamWriter(server.getOutputStream(), StandardCharsets.UTF_8), true);
            toServer.print(message);
            toServer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
