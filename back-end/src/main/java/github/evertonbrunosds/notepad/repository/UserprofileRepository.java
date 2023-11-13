package github.evertonbrunosds.notepad.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;

public interface UserprofileRepository extends JpaRepository<UserprofileEntity, UUID> {

    public Optional<UserprofileEntity> findByEmail(final String email);

}
