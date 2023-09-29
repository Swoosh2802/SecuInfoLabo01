import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4200)) {
            while (true) {
                Socket client = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                System.out.println(readLine(fromClient));
            }
        } catch (IOException exception) {
            System.out.println("Erreur serveur: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null && line.length() > 2 && line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }
}
