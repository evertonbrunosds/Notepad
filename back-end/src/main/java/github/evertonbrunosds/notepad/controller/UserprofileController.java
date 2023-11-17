package github.evertonbrunosds.notepad.controller;

import static github.evertonbrunosds.notepad.util.Parameter.EMAIL;
import static github.evertonbrunosds.notepad.util.Parameter.ID_USERPROFILE_PK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Controller;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.security.model.JWTBuilder;
import github.evertonbrunosds.notepad.security.model.JWTBuilder.JWTAuthentication;
import github.evertonbrunosds.notepad.service.UserprofileService;
import github.evertonbrunosds.notepad.util.ResourceException;
import github.evertonbrunosds.notepad.util.Validate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//NOT NULL
//CHECK
@Controller
@RequiredArgsConstructor
public class UserprofileController {

    private final UserprofileService service;

    private final JWTBuilder jwtBuilder;

    private final Validate validate = new Validate(UserprofileController.class);

    public JWTAuthentication authenticate(final Consumer<AuthenticationDataManager> content) {
        final var dataManager = new AuthenticationDataManager();
        content.accept(dataManager);
        validate.email(dataManager.email);
        validate.password(dataManager.password);
        return jwtBuilder.build(dataManager.email, dataManager.password, dataManager.expirationTime);
    }

    public UserprofileEntity create(final UserprofileEntity userprofile) {
        validate.username(userprofile.getUsername());
        validate.birthday(userprofile.getBirthday());
        validate.email(userprofile.getEmail());
        validate.password(userprofile.getPassword());
        return service.create(userprofile);
    }

    public UserprofileEntity update(final UserprofileEntity userprofile) {
        validate.username(userprofile.getUsername());
        validate.birthday(userprofile.getBirthday());
        validate.email(userprofile.getEmail());
        validate.password(userprofile.getPassword());
        return service.update(userprofile);
    }


    public UserprofileEntity findByEmail(final String email) {
        validate.email(email);
        final var result = service.findByEmail(email);
        return result.orElseThrow(() -> {
            return new ResourceException(NOT_FOUND, EMAIL, UserprofileController.class);
        });
    }

    public UserprofileEntity findByIdUserprofilePk(final UUID idUserprofilePk) {
        final var result = service.findByIdUserprofilePk(idUserprofilePk);
        return result.orElseThrow(() -> {
            return new ResourceException(NOT_FOUND, ID_USERPROFILE_PK, UserprofileController.class);
        });
    }

    public void deleteByIdUserprofilePk(final UUID idUserprofilePk) {
        service.deleteByIdUserprofilePk(idUserprofilePk);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Setter
    public class AuthenticationDataManager {

        private String email;

        private String password;

        private Integer expirationTime;

    }

}
