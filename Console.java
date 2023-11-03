import Implementations.AESImplementation;
import Implementations.ThreeDesImplementation;

public class Console {
    java.io.Console console = System.console();

    public void initConsole() throws Exception {
        String choosenAlgorithm = console.readLine(
                "Quel algorithme utilisez-vous?\n 1) 3DES \n 2) AES \n 3) SHA-1 \n");
        readChoice(choosenAlgorithm);
    }

    private void readChoice(String choice) throws Exception {
        String mode = "";
        switch (choice) {
            case "1":
            ThreeDesImplementation tripleDes = new ThreeDesImplementation();
            while (mode.equals("")) {
                mode = console.readLine("1) Chiffrer\n 2) Déchiffrer \n");
                switch (mode) {
                    case "1":
                        String toCypher = console.readLine("Entrez le texte à chiffrer");
                        Client.sendMessage(tripleDes.encrypt(toCypher));
                        System.out.println(tripleDes.encrypt(toCypher));
                        break;
                    case "2":
                        String toUncypher = console.readLine("Entrez le texte à déchiffrer");
                        tripleDes.decrypt(toUncypher);
                        System.out.println(tripleDes.decrypt(toUncypher));
                        break;
                    default:
                        mode = "";
                }
            }
            break;
            case "2":
                AESImplementation aesAlgo = new AESImplementation();
                while (mode.equals("")) {
                    mode = console.readLine("1) Chiffrer\n 2) Déchiffrer \n");
                    switch (mode) {
                        case "1":
                            String toCypher = console.readLine("Entrez le texte à chiffrer");
                            Client.sendMessage(aesAlgo.encrypt(toCypher));
                            System.out.println(aesAlgo.encrypt(toCypher));
                            break;
                        case "2":
                            String toUncypher = console.readLine("Entrez le texte à déchiffrer");
                            aesAlgo.decrypt(toUncypher);
                            System.out.println(aesAlgo.decrypt(toUncypher));
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
                initConsole();
        }
    }
}
