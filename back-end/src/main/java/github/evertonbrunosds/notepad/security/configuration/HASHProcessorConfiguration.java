package github.evertonbrunosds.notepad.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

@Configuration
public class HASHProcessorConfiguration {

    @Getter(onMethod_ = @Bean)
    private final PasswordEncoder passwordEncoder;

    public HASHProcessorConfiguration() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

}
