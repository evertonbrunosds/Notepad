package github.evertonbrunosds.notepad.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import github.evertonbrunosds.notepad.security.model.JWTBuilder;
import github.evertonbrunosds.notepad.security.model.JWTLoader;
import github.evertonbrunosds.notepad.service.UserprofileService;
import lombok.Getter;

@Configuration
@PropertySource(value = "classpath:jwt.properties")
public class JWTConfiguration {

    private static final String DEFAULT = "";

    private static final String KEY = "jwt.key";

    @Getter(onMethod_ = @Bean)
    private final JWTLoader jwtLoader;

    @Getter(onMethod_ = @Bean)
    private final JWTBuilder jwtBuilder;

    public JWTConfiguration(final Environment environment, final UserprofileService service, final AuthenticationConfiguration configuration, final AuthenticationManagerBuilder builder, final PasswordEncoder encoder) {
        this.jwtBuilder = new JWTBuilder(environment.getProperty(KEY, DEFAULT), userDetailsService(service), configuration, builder, encoder);
        this.jwtLoader = new JWTLoader(environment.getProperty(KEY, DEFAULT).getBytes());
    }

    public UsernameNotFoundException UsernameNotFoundException() {
        return new UsernameNotFoundException("NOT_FOUND");
    }

    private UserDetailsService userDetailsService(final UserprofileService service) {
        return (email) -> {
            return service.findByEmail(email).orElseThrow(this::UsernameNotFoundException);
        };
    }

}
