package ar.edu.itba.paw.webapp.validators;

public interface Validator<T> {

    void validate(T objectToValidate);

    default Validator<T> and(Validator<T> nextValidator) {
        return (objectToValidate) -> {
            this.validate(objectToValidate);
            if (nextValidator != null) {
                nextValidator.validate(objectToValidate);
            }
        };
    }
}
