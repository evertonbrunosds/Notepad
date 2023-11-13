package github.evertonbrunosds.notepad.security.model;

import static github.evertonbrunosds.notepad.util.Parameter.AUTHOR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import github.evertonbrunosds.notepad.model.shared.UserprofileShared;
import github.evertonbrunosds.notepad.util.ResourceException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Author {

    private final UserprofileShared user;

    public UserprofileShared getUserprofile() {
        if (user == null) {
            throw new ResourceException(UNAUTHORIZED, AUTHOR, Author.class);
        } else {
            return user;
        }
    }

}
