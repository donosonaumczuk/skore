package ar.edu.itba.paw.exceptions;

public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(final String resourceType, final String resourceId) {
        super(resourceType + " '" + resourceId + "' not found");
    }

    public EntityNotFoundException(final String resourceType, final String idAttributeName, final String idAttributeValue) {
        super("No " + resourceType + " found with " + idAttributeName + " '" + idAttributeValue + "'");
    }
}
