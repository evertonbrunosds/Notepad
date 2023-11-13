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
import github.evertonbrunosds.notepad.model.shared.UserprofileShared;
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

    public UserprofileShared create(final UserprofileShared shared) {
        var entity = toEntity(shared);
        if (repository.findByEmail(entity.getEmail()).isPresent()) {
            throw new ResourceException(CONFLICT, EMAIL, UserprofileService.class);
        } else {
            entity.setIdUserprofilePk(null);
            entity.setCreated(currentLocalDateTime());
            entity = repository.save(entity);
            return toShared(entity);
        }
    }

    public UserprofileShared update(final UserprofileShared shared) {
        findByEmail(shared.getEmail()).ifPresent(entity -> {
            if (!entity.getIdUserprofilePk().equals(shared.getIdUserprofilePk())) {
                throw new ResourceException(CONFLICT, EMAIL, UserprofileService.class);
            }
        });
        var entity = toEntity(shared);
        entity = repository.save(entity);
        return toShared(entity);
    }

    public Optional<UserprofileShared> findByEmail(final String email) {
        final var encodedEmail = aes.encode(email).orThrow(this::emailResourceException);
        final var result = repository.findByEmail(encodedEmail);
        return result.isPresent()
                ? Optional.of(toShared(result.get()))
                : Optional.empty();
    }

    public Optional<UserprofileShared> findByIdUserprofilePk(final UUID idUserprofilePk) {
        final var result = repository.findById(idUserprofilePk);
        return result.isPresent()
                ? Optional.of(toShared(result.get()))
                : Optional.empty();
    }

    public void deleteByIdUserprofilePk(final UUID idUserprofilePk) {
        repository.deleteById(idUserprofilePk);
    }

    private UserprofileShared toShared(final UserprofileEntity entity) {
        return UserprofileShared.builder()
                .idUserprofilePk(entity.getIdUserprofilePk())
                .username(entity.getUsername())
                .birthday(entity.getBirthday())
                .password(entity.getPassword())
                .created(entity.getCreated())
                .email(aes.decode(entity.getEmail()).orThrow(this::emailResourceException))
                .build();
    }

    private UserprofileEntity toEntity(final UserprofileShared shared) {
        return UserprofileEntity.builder()
                .idUserprofilePk(shared.getIdUserprofilePk())
                .username(shared.getUsername())
                .birthday(shared.getBirthday())
                .password(passwordEncoder.encode(shared.getPassword()))
                .created(shared.getCreated())
                .email(aes.encode(shared.getEmail()).orThrow(this::emailResourceException))
                .build();
    }

    private ResourceException emailResourceException() {
        return new ResourceException(INTERNAL_SERVER_ERROR, EMAIL, UserprofileService.class);
    }

}
