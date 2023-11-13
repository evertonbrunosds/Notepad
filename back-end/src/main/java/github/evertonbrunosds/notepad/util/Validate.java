package github.evertonbrunosds.notepad.util;

import static github.evertonbrunosds.notepad.util.LocalDateManager.getYears;
import static github.evertonbrunosds.notepad.util.Parameter.BIRTHDAY;
import static github.evertonbrunosds.notepad.util.Parameter.EMAIL;
import static github.evertonbrunosds.notepad.util.Parameter.PASSWORD;
import static github.evertonbrunosds.notepad.util.Parameter.USERNAME;
import static github.evertonbrunosds.notepad.util.Validator.ifNotEmail;
import static github.evertonbrunosds.notepad.util.Validator.ifNull;
import static github.evertonbrunosds.notepad.util.Validator.ifTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Validate {

    private final Class<?> involvedClass;

    public void username(final String username) {
        ifNull(username).throwResourceException(BAD_REQUEST, USERNAME, getClass());
        ifTrue(username.length() > 64).throwResourceException(BAD_REQUEST, USERNAME, involvedClass);
    }

    public void birthday(final LocalDate birthday) {
        ifNull(birthday).throwResourceException(BAD_REQUEST, BIRTHDAY, involvedClass);
        ifTrue(getYears(birthday) < 14).throwResourceException(FORBIDDEN, BIRTHDAY, involvedClass);
    }

    public void email(final String email) {
        ifNull(email).throwResourceException(BAD_REQUEST, EMAIL, involvedClass);
        ifTrue(email.length() > 190).throwResourceException(BAD_REQUEST, EMAIL, involvedClass);
        ifNotEmail(email).throwResourceException(BAD_REQUEST, EMAIL, involvedClass);
    }

    public void password(final String password) {
        ifNull(password).throwResourceException(BAD_REQUEST, PASSWORD, involvedClass);
        ifTrue(password.length() < 8).throwResourceException(BAD_REQUEST, PASSWORD, involvedClass);
        ifTrue(password.length() > 512).throwResourceException(BAD_REQUEST, PASSWORD, involvedClass);
    }

}
