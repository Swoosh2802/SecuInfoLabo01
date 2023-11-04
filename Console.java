import Implementations.AESImplementation;
import Implementations.ThreeDesImplementation;

public class Console {
    java.io.Console console = System.console();
    ThreeDesImplementation tripleDes;
    AESImplementation aesAlgo;

    public Console() throws Exception {
        tripleDes = new ThreeDesImplementation();
        aesAlgo = new AESImplementation();
        while(true){
            String choosenAlgorithm = console.readLine("Quel algorithme utilisez-vous?\n 1) 3DES \n 2) AES \n 3) SHA-1 \n");
            readChoice(choosenAlgorithm);
        }
    }

    private void readChoice(String choice) throws Exception {
        String mode = "";
        switch (choice) {
            case "1":
            while (mode.equals("")) {
                mode = console.readLine("1) Chiffrer\n 2) Déchiffrer \n");
                switch (mode) {
                    case "1":
                        String toCypher = console.readLine("Entrez le texte à chiffrer");
                        String ciphered = tripleDes.encrypt(toCypher);
                        Client.sendMessage(ciphered);
                        System.out.println(ciphered);
                        break;
                    case "2":
                        String toUncypher = console.readLine("Entrez le texte à déchiffrer");
                        String unciphered = tripleDes.decrypt(toUncypher);
                        Client.sendMessage(unciphered);
                        System.out.println(unciphered);
                        break;
                    default:
                        mode = "";
                }
            }
            break;
            case "2":
                while (mode.equals("")) {
                    mode = console.readLine("1) Chiffrer\n 2) Déchiffrer \n");
                    switch (mode) {
                        case "1":
                            String toCypher = console.readLine("Entrez le texte à chiffrer");
                            String ciphered = aesAlgo.encrypt(toCypher);
                            Client.sendMessage(ciphered);
                            System.out.println(ciphered);
                            break;
                        case "2":
                            String toUncypher = console.readLine("Entrez le texte à déchiffrer");
                            String unciphered = aesAlgo.decrypt(toUncypher);
                            Client.sendMessage(unciphered);
                            System.out.println(unciphered);
                            break;
                        default:
                            mode = "";
                    }
                }
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                readChoice(choice);
        }
    }
}
