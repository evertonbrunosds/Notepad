package github.evertonbrunosds.notepad.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity(name = "userprofile")
@Table(schema = "public")
@NoArgsConstructor
@SuperBuilder
public class UserprofileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_userprofile_pk", updatable = false)
    private UUID idUserprofilePk;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "encoded_email_un", length = 256, nullable = false, unique = true)
    private String email;

    @Column(name = "encoded_password", length = 60, nullable = false)
    private String password;

    @Column(name = "created", updatable = false, nullable = false)
    private LocalDateTime created;

}
