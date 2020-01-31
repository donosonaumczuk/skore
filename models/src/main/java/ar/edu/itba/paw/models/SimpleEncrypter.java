package ar.edu.itba.paw.models;

import javassist.tools.reflect.CannotInvokeException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SimpleEncrypter {

    private Cipher cipher;
    private SecretKey key;

    private static final String INSTANCE_KEY = "AES";

    public SimpleEncrypter() {
        try {
            KeyGenerator.getInstance(INSTANCE_KEY).init(128);
            byte[] fixedKey = new byte[] {49, 74, 32, -79, 89, -46, 80, -120, -81, -5, -42, 33, 11, -82, 48, 25};
            key = new SecretKeySpec(fixedKey, INSTANCE_KEY);
            cipher = Cipher.getInstance(INSTANCE_KEY);
        }
        catch(Exception e) {
            throw new CannotInvokeException("Can't instanciate encrypter");
        }
    }


    public String encryptString(String textToBeEncrypted) {
        String encryptedString;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] encryptedBytes = cipher.doFinal(textToBeEncrypted.getBytes());
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch(Exception e) {
            throw new CannotInvokeException("Can't instanciate encrypter");
        }

        return encryptedString;
    }

    public String decryptString(String textToBeDecrypted) {
        String decryptedString;

        try {

            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] decodedBytes = Base64.getDecoder().decode(textToBeDecrypted);
            final byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            decryptedString = new String(decryptedBytes);
        } catch(Exception ex) {
            throw new CannotInvokeException("Can't instanciate encrypter");
        }

        return decryptedString;
    }
}

