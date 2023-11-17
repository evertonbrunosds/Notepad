package github.evertonbrunosds.notepad.security.model;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionState {

    private Supplier<Collection<? extends GrantedAuthority>> authorities;

    private Supplier<Boolean> credentialsNonExpired;

    private Supplier<Boolean> accountNonExpired;

    private Supplier<Boolean> accountNonLocked;

    private Supplier<Boolean> enabled;

    public UserSessionState() {
        authorities = () -> Collections.emptyList();
        credentialsNonExpired = () -> Boolean.TRUE;
        accountNonExpired = () -> Boolean.TRUE;
        accountNonLocked = () -> Boolean.TRUE;
        enabled = () -> Boolean.TRUE;
    }

    public void loadFromInstance(final UserSessionState state) {
        this.authorities = state.authorities;
        this.credentialsNonExpired = state.credentialsNonExpired;
        this.accountNonExpired = state.accountNonExpired;
        this.accountNonLocked = state.accountNonLocked;
        this.enabled = state.enabled;
    }

}
