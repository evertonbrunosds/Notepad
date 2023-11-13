package github.evertonbrunosds.notepad.security.model;

import static github.evertonbrunosds.notepad.util.Parameter.PASSWORD;
import static github.evertonbrunosds.notepad.util.Parameter.SERVICE;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import github.evertonbrunosds.notepad.model.shared.UserprofileShared;
import github.evertonbrunosds.notepad.util.ResourceException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTBuilder {

    private final static Class<?> INVOLVED_CLASS = JWTBuilder.class;

    private final byte[] key;

    private final AuthenticationConfiguration configuration;

    public JWTBuilder(
        final String key,
        final UserDetailsService service,
        final AuthenticationConfiguration configuration,
        final AuthenticationManagerBuilder builder,
        final PasswordEncoder encoder
    ) {
        this.key = key.getBytes();
        this.configuration = configuration;
        try {
            builder.userDetailsService(service).passwordEncoder(encoder);
        } catch (final ResourceException resourceException) {
            throw resourceException;
        } catch (final Exception exception) {
            throw new ResourceException(INTERNAL_SERVER_ERROR, SERVICE, getClass());
        }
    }

    public String build(final String email, final String password, final Integer expirationTime) {
        try {
            final var manager = configuration.getAuthenticationManager();
            final var token = new UsernamePasswordAuthenticationToken(email, password, emptyList());
            final var authentication = manager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final var currentDate = new Date();
            final var expirationDate = new Date(currentDate.getTime() + expirationTime);
            final var userprofile = (UserprofileShared) authentication.getPrincipal();
            return Jwts.builder()
                    .setSubject(userprofile.getIdUserprofilePk().toString())
                    .setIssuedAt(currentDate)
                    .setExpiration(expirationDate)
                    .signWith(new SecretKeySpec(key, SignatureAlgorithm.HS512.getJcaName()))
                    .compact();
        } catch (final Throwable throwable) {
            throw new ResourceException(UNAUTHORIZED, PASSWORD, INVOLVED_CLASS);
        }
    }

}
