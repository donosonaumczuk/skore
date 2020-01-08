package ar.edu.itba.paw.Exceptions;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException (String message) {
        super(message);
    }
}
