package github.evertonbrunosds.notepad.security.model;

import static github.evertonbrunosds.notepad.util.Parameter.ACCOUNT_EXPIRED;
import static github.evertonbrunosds.notepad.util.Parameter.ACCOUNT_LOCKED;
import static github.evertonbrunosds.notepad.util.Parameter.CREDENTIALS_EXPIRED;
import static github.evertonbrunosds.notepad.util.Parameter.DISABLED;
import static github.evertonbrunosds.notepad.util.Parameter.EMAIL;
import static github.evertonbrunosds.notepad.util.Parameter.PASSWORD;
import static github.evertonbrunosds.notepad.util.Parameter.SERVICE;
import static github.evertonbrunosds.notepad.util.Parameter.UNDETERMINED;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.time.LocalDateTime;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.util.ResourceException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTBuilder {

    private final static Class<?> INVOLVED_CLASS = JWTBuilder.class;

    private final byte[] key;

    private final AuthenticationConfiguration configuration;

    private final UserDetailsService service;

    public JWTBuilder(
        final String key,
        final UserDetailsService service,
        final AuthenticationConfiguration configuration,
        final AuthenticationManagerBuilder builder,
        final PasswordEncoder encoder
    ) {
        this.key = key.getBytes();
        this.configuration = configuration;
        this.service = service;
        try {
            builder.userDetailsService(service).passwordEncoder(encoder);
        } catch (final ResourceException resourceException) {
            throw resourceException;
        } catch (final Exception exception) {
            throw new ResourceException(INTERNAL_SERVER_ERROR, SERVICE, getClass());
        }
    }

    public JWTAuthentication build(final String email, final String password, final Integer expirationTime) {
        try {
            final var manager = configuration.getAuthenticationManager();
            final var token = new UsernamePasswordAuthenticationToken(email, password, emptyList());
            final var authentication = manager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final var currentDate = new Date();
            final var expirationDateTime = LocalDateTime.now().plusSeconds(expirationTime / 1000);
            final var expirationDate = new Date(currentDate.getTime() + expirationTime);
            final var userprofile = (UserprofileEntity) authentication.getPrincipal();
            return new JWTAuthentication(Jwts.builder()
                    .setSubject(userprofile.getIdUserprofilePk().toString())
                    .setIssuedAt(currentDate)
                    .setExpiration(expirationDate)
                    .signWith(new SecretKeySpec(key, SignatureAlgorithm.HS512.getJcaName()))
                    .compact(),
                    userprofile,
                    expirationDateTime);
        } catch (final Throwable throwable) {
            exceptionCause(email);
            throw new ResourceException(INTERNAL_SERVER_ERROR, UNDETERMINED, INVOLVED_CLASS);
        }
    }

    public void exceptionCause(final String email) {
        try {
            final var userDetails = service.loadUserByUsername(email);
            if (!userDetails.isCredentialsNonExpired()) {
                throw new ResourceException(UNAUTHORIZED, CREDENTIALS_EXPIRED, INVOLVED_CLASS);
            } else if (!userDetails.isAccountNonExpired()) {
                throw new ResourceException(UNAUTHORIZED, ACCOUNT_EXPIRED, INVOLVED_CLASS);
            } else if (!userDetails.isAccountNonLocked()) {
                throw new ResourceException(UNAUTHORIZED, ACCOUNT_LOCKED, INVOLVED_CLASS);
            } else if (!userDetails.isEnabled()) {
                throw new ResourceException(UNAUTHORIZED, DISABLED, INVOLVED_CLASS);
            } else {
                throw new ResourceException(UNAUTHORIZED, PASSWORD, INVOLVED_CLASS);
            }
        } catch (final UsernameNotFoundException exception) {
            throw new ResourceException(NOT_FOUND, EMAIL, INVOLVED_CLASS);
        }
    }

    public record JWTAuthentication(String token, UserprofileEntity userprofile, LocalDateTime timeLimit) {}

}
