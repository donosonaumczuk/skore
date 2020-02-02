package ar.edu.itba.paw.exceptions.alreadyexists;

public abstract class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(final String entityName, final String entityId) {
        super(entityName + " '" + entityId + "' already exists");
    }

    public EntityAlreadyExistsException(final String resourceType, final String idAttributeName,
                                        final String idAttributeValue) {
        super(resourceType + " with " + idAttributeName + " '" + idAttributeValue + "' already exists");
    }
}
