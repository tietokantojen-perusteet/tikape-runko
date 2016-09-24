package tikape.runko;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64.Decoder;

/**
 *
 * @author Aleksi Huotala
 */
public class Auth {

    public static boolean passwordMatches(String password, String hashedPasswordBase64, String saltBase64) throws NoSuchAlgorithmException {
        Decoder en = java.util.Base64.getDecoder();
        try {
            byte[] passwordBytes = password.getBytes("UTF-8");
            //Muutetaan suola bytearrayksi
            byte[] salt = en.decode(saltBase64);
            //Viedään tietokannan salasana bytearrayksi
            byte[] hashedPassword = en.decode(hashedPasswordBase64);
            //Yhdistetään suola ja salasana
            byte[] passwordWithSalt = Auth.combineTwoByteArrays(salt, passwordBytes);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Salataan käyttäjän syöttämä salasana
            byte[] hashedPasswordWithSalt = digest.digest(passwordWithSalt);
            return Arrays.equals(hashedPasswordWithSalt, hashedPassword);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static byte[] combineTwoByteArrays(byte[] bytes1, byte[] bytes2) {
        byte[] byteArray = new byte[bytes1.length + bytes2.length];
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = i < bytes1.length ? bytes1[i] : bytes2[i - bytes1.length];
        }
        return byteArray;
    }
}
