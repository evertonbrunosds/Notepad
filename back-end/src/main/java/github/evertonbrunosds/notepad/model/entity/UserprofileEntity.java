package github.evertonbrunosds.notepad.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import github.evertonbrunosds.notepad.security.model.UserSessionState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
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
public class UserprofileEntity implements UserDetails {

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

    @Builder.Default
    private final transient UserSessionState state = new UserSessionState();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return state.getAuthorities().get();
    }

    @Override
    public boolean isAccountNonExpired() {
        return state.getAccountNonExpired().get();
    }

    @Override
    public boolean isAccountNonLocked() {
        return state.getAccountNonLocked().get();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return state.getCredentialsNonExpired().get();
    }

    @Override
    public boolean isEnabled() {
        return state.getEnabled().get();
    }

    public final UserprofileEntity copyWithOriginalState() {
        return builder()
                .idUserprofilePk(idUserprofilePk)
                .username(username)
                .birthday(birthday)
                .email(email)
                .password(password)
                .created(created)
                .state(state)
                .build();
    }

}
