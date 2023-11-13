package github.evertonbrunosds.notepad.controller;

import static github.evertonbrunosds.notepad.util.Parameter.EMAIL;
import static github.evertonbrunosds.notepad.util.Parameter.ID_USERPROFILE_PK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Controller;

import github.evertonbrunosds.notepad.model.shared.UserprofileShared;
import github.evertonbrunosds.notepad.security.model.JWTBuilder;
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

    public String authenticate(final Consumer<DataAuth> content) {
        final var dataAuth = new DataAuth();
        content.accept(dataAuth);
        validate.email(dataAuth.email);
        validate.password(dataAuth.password);
        return jwtBuilder.build(dataAuth.email, dataAuth.password, dataAuth.expirationTime);
    }

    public UserprofileShared create(final UserprofileShared shared) {
        validate.username(shared.getUsername());
        validate.birthday(shared.getBirthday());
        validate.email(shared.getEmail());
        validate.password(shared.getPassword());
        return service.create(shared);
    }

    public UserprofileShared update(final UserprofileShared shared) {
        validate.username(shared.getUsername());
        validate.birthday(shared.getBirthday());
        validate.email(shared.getEmail());
        validate.password(shared.getPassword());
        return service.update(shared);
    }


    public UserprofileShared findByEmail(final String email) {
        validate.email(email);
        final var result = service.findByEmail(email);
        return result.orElseThrow(() -> {
            return new ResourceException(NOT_FOUND, EMAIL, UserprofileController.class);
        });
    }

    public UserprofileShared findByIdUserprofilePk(final UUID idUserprofilePk) {
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
    public class DataAuth {

        private String email;

        private String password;

        private Integer expirationTime;

    }

}
