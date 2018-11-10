package ar.edu.itba.paw.models;

public class SimpleEncrypter {

    public  String simpleCipherEncrypt(String phrase) {
        String encriptedPhrase= "";
        for(int i = 0; i < phrase.length(); i++) {
            if((phrase.charAt(i) >= 'a' && phrase.charAt(i) <= 'z') ||
                    (phrase.charAt(i) >= 'A' && phrase.charAt(i) <= 'Z') ) {
                if (phrase.charAt(i) == 'z') {
                    encriptedPhrase += 'a';
                } else if (phrase.charAt(i) == 'Z') {
                    encriptedPhrase += 'A';
                } else {
                    encriptedPhrase += (char) (phrase.charAt(i) + 1);
                }
            }
            else {
                encriptedPhrase += phrase.charAt(i);
            }
        }
        return encriptedPhrase;
    }

    public String simpleCipherDecrypt(String encriptedPhrase) {
        String decriptedPhrase= "";
        for(int i = 0; i <  encriptedPhrase.length(); i++) {
            if((encriptedPhrase.charAt(i) >= 'a' && encriptedPhrase.charAt(i) <= 'z') ||
                    (encriptedPhrase.charAt(i) >= 'A' && encriptedPhrase.charAt(i) <= 'Z') ) {
                if (encriptedPhrase.charAt(i) == 'a') {
                    decriptedPhrase += 'z';
                } else if (encriptedPhrase.charAt(i) == 'A') {
                    decriptedPhrase += 'Z';
                }
                else {
                    decriptedPhrase += (char) (encriptedPhrase.charAt(i) - 1);
                }
            }
            else {
                decriptedPhrase += encriptedPhrase.charAt(i);
            }
        }
        return decriptedPhrase;
    }
}
