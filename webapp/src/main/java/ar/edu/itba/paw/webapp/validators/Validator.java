package ar.edu.itba.paw.webapp.validators;

public interface Validator<T> {

    void validate(T t);

    default Validator<T> and(Validator<T> v) {
        return (t) -> {
            this.validate(t);
            if (v != null) {
                v.validate(t);
            }
        };
    }
}
