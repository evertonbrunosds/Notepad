package github.evertonbrunosds.notepad.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import github.evertonbrunosds.notepad.security.model.SymmetricSecureProcessor;
import lombok.Getter;

@Configuration
@PropertySource(value = "classpath:symmetric-secure.properties", encoding = "UTF-8")
public class SymmetricSecureProcessorConfiguration {

    private static final String DEFAULT = "";

    @Getter(onMethod_ = @Bean)
    private final AES aes;

    public SymmetricSecureProcessorConfiguration(final Environment environment) {
        this.aes = new AES(
            environment.getProperty(AES.TRANSFORMATION, DEFAULT),
            environment.getProperty(AES.KEY, DEFAULT).getBytes()
        );
    }

    public class AES extends SymmetricSecureProcessor {

        private static final String ALGORITHM = "AES";

        private static final String TRANSFORMATION = "aes.transformation";

        private static final String KEY = "aes.key";

        public AES(final String transformation, final byte[] key) {
            super(ALGORITHM, transformation, key);
        }

    }

}
