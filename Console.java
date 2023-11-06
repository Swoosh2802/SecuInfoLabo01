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

    public Console() throws Exception {
        tripleDes = new ThreeDesImplementation();
        aesAlgo = new AESImplementation();
        sha1Implementation = new SHA1Implementation();
        hmacmd5 = new HMACMD5();
        while(true){
            String choosenAlgorithm = console.readLine("Quel algorithme utilisez-vous?\n 1) 3DES \n 2) AES \n");
            readChoice(choosenAlgorithm);
        }
    }

    private void readChoice(String choice) throws Exception {
        String mode = "";
        switch (choice) {
            case "1":
                while (mode.equals("")) {
                    mode = console.readLine("1) Chiffrer\n2) Déchiffrer \n");
                    switch (mode) {
                        case "1":
                            String toCypher = console.readLine("Entrez le texte à chiffrer\n");

                            HMACMD5 hmac = new HMACMD5();
                            String calculatedHMAC = hmac.calculateHMAC(toCypher); //Ajout de l'authentification HMAC-MD5
                            toCypher = toCypher+calculatedHMAC; //Ajout de l'authentification HMAC-MD5
                            
                            String ciphered = tripleDes.encrypt(toCypher);
                            Client.sendMessage("3DE:"+ciphered);
                            Client.sendMessage(sha1Implementation.sha1Hash("3DE:"+ciphered));
                            break;
                        default:
                            mode = "";
                    }
                }
                break;
            case "2":
                while (mode.equals("")) {
                    mode = console.readLine("1) Chiffrer\n2) Déchiffrer \n");
                    switch (mode) {
                        case "1":
                            String toCypher = console.readLine("Entrez le texte à chiffrer\n");
                            String ciphered = aesAlgo.encrypt(toCypher);
                            Client.sendMessage("AES:"+ciphered);
                            Client.sendMessage(sha1Implementation.sha1Hash("AES:"+ciphered));
                            break;
                        default:
                            mode = "";
                    }
                }
                break;
            default:
                readChoice(choice);
        }
    }
}
