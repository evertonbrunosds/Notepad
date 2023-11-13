package github.evertonbrunosds.notepad.util;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

public class Validator {

    private static final String EMAIL_FORMAT = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public final static Thrower ifNotEmail(final String email) {
        return ifTrue(!Pattern.compile(EMAIL_FORMAT).matcher(email).matches());
    }

    public final static Thrower ifEmail(final String email) {
        return ifTrue(Pattern.compile(EMAIL_FORMAT).matcher(email).matches());
    }

    public final static Thrower ifNull(final Object target) {
        return ifTrue(target == null);
    }

    public final static Thrower ifNotNull(final Object target) {
        return ifTrue(target != null);
    }

    public final static Thrower ifFalse(final boolean target) {
        return ifTrue(!target);
    }

    public final static Thrower ifTrue(final boolean target) {
        return new Thrower() {

            @Override
            public void throwResourceException(final HttpStatus involvedStatus, final Parameter involvedParameter,
                    final Class<?> involvedClass) {
                if (target) {
                    throw new ResourceException(involvedStatus, involvedParameter, involvedClass);
                }
            }

        };
    }

    @FunctionalInterface
    public interface Thrower {

        public void throwResourceException(final HttpStatus involvedStatus, final Parameter involvedParameter, final Class<?> involvedClass);

    }

}
