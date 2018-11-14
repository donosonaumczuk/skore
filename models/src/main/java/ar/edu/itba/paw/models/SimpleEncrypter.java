package ar.edu.itba.paw.models;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleEncrypter {
    private Cipher cipher;
    private SecretKey key;

    public SimpleEncrypter() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            byte fixedKey [] = new byte[] {49, 74, 32, -79, 89, -46, 80, -120, -81, -5, -42,
                                            33, 11, -82, 48, 25};
            key = new SecretKeySpec(fixedKey, "AES");
            cipher = Cipher.getInstance("AES");
        }
        catch(Exception e) {
            System.out.println("Can't instanciate encrypter");
        }
    }


    public String encriptString(String textToBeEncrypted) {
        String encryptedString = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] encriptedBytes = cipher.doFinal(textToBeEncrypted.getBytes());
            encryptedString = new BASE64Encoder().encode(encriptedBytes);
        }
        catch(Exception e) {
            System.out.println("Can't encrypt");
        }

        return encryptedString;

    }

    public String decryptString(String textToBeDecrypted) {
        String decryptedString = null;

        try {

            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] decodedBytes = new BASE64Decoder().decodeBuffer(textToBeDecrypted);
            final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            decryptedString = new String(decryptedBytes);
        } catch(Exception ex) {
            System.out.println("The Exception is=" + ex);
        }

        return decryptedString;
    }
}

