package by.babanin.batchcopy.application.validator;

import java.util.List;

@FunctionalInterface
public interface Validator<T> {

    List<String> validate(T component);

    default boolean isValid(T component) {
        return validate(component).isEmpty();
    }
}
