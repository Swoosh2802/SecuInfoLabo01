package Implementations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Implementation {

    public SHA1Implementation(){}

    public String sha1Hash(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = sha1.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) throws Exception {
        SHA1Implementation sha = new SHA1Implementation();
        String target = "Hello World";
        String test1 = sha.sha1Hash(target);
        String test2 = sha.sha1Hash(target);
        System.out.println("String To Encrypt:" + target);
        System.out.println("1ere génération de hash:" + test1);
        System.out.println("2eme génération de hash:" + test2);
        System.out.println("hash identiques?: "+ test1.equals(test2));
    }
}
