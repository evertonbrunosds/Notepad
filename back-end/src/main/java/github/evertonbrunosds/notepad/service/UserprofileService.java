package github.evertonbrunosds.notepad.service;

import static github.evertonbrunosds.notepad.util.LocalDateTimeManager.currentLocalDateTime;
import static github.evertonbrunosds.notepad.util.Parameter.EMAIL;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.repository.UserprofileRepository;
import github.evertonbrunosds.notepad.security.configuration.SymmetricSecureProcessorConfiguration.AES;
import github.evertonbrunosds.notepad.util.ResourceException;
import lombok.RequiredArgsConstructor;

//UNIQUE
//DEFAULT
@Service
@RequiredArgsConstructor
public class UserprofileService {

    private final AES aes;

    private final UserprofileRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserprofileEntity create(final UserprofileEntity userDecoded) {
        final var userEncoded = toEncode(userDecoded);
        if (repository.findByEmail(userEncoded.getEmail()).isPresent()) {
            throw new ResourceException(CONFLICT, EMAIL, UserprofileService.class);
        } else {
            userEncoded.setIdUserprofilePk(null);
            userEncoded.setCreated(currentLocalDateTime());
            final var userCreated = repository.save(userEncoded);
            return toDecode(userCreated);
        }
    }

    public UserprofileEntity update(final UserprofileEntity userDecoded) {
        final var userEncoded = toEncode(userDecoded);
        repository.findByEmail(userEncoded.getEmail()).ifPresent(currentUser -> {
            if (!currentUser.getIdUserprofilePk().equals(userEncoded.getIdUserprofilePk())) {
                throw new ResourceException(CONFLICT, EMAIL, UserprofileService.class);
            }
            userEncoded.getState().loadFromInstance(currentUser.getState());
        });
        final var userUpdated = repository.save(userEncoded);
        return toDecode(userUpdated);
    }

    public Optional<UserprofileEntity> findByEmail(final String email) {
        final var encodedEmail = aes.encode(email).orThrow(this::emailResourceException);
        final var result = repository.findByEmail(encodedEmail);
        return result.isPresent()
                ? Optional.of(toDecode(result.get()))
                : Optional.empty();
    }

    public Optional<UserprofileEntity> findByIdUserprofilePk(final UUID idUserprofilePk) {
        final var result = repository.findById(idUserprofilePk);
        return result.isPresent()
                ? Optional.of(toDecode(result.get()))
                : Optional.empty();
    }

    public void deleteByIdUserprofilePk(final UUID idUserprofilePk) {
        repository.deleteById(idUserprofilePk);
    }

    public UserprofileEntity toDecode(final UserprofileEntity userEncoded) {
        final var userDecoded = userEncoded.copyWithOriginalState();
        userDecoded.setEmail(aes.decode(userEncoded.getEmail()).orThrow(this::emailResourceException));
        return userDecoded;
    }

    public UserprofileEntity toEncode(final UserprofileEntity userDecoded) {
        final var userEncoded = userDecoded.copyWithOriginalState();
        userEncoded.setEmail(aes.encode(userDecoded.getEmail()).orThrow(this::emailResourceException));
        userEncoded.setPassword(passwordEncoder.encode(userDecoded.getPassword()));
        return userEncoded;
    }

    private ResourceException emailResourceException() {
        return new ResourceException(INTERNAL_SERVER_ERROR, EMAIL, UserprofileService.class);
    }

}
