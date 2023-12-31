package github.evertonbrunosds.notepad.security.model;

import static github.evertonbrunosds.notepad.util.Parameter.AUTHOR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.util.ResourceException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Author {

    private final UserprofileEntity user;

    public UserprofileEntity getUserprofile() {
        if (user == null) {
            throw new ResourceException(UNAUTHORIZED, AUTHOR, Author.class);
        } else {
            return user;
        }
    }

}
