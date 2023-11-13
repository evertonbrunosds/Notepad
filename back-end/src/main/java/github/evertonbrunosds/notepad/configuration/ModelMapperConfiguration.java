package github.evertonbrunosds.notepad.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class ModelMapperConfiguration {

    @Getter(onMethod_ = @Bean)
    private final ModelMapper modelMapper;

    public ModelMapperConfiguration() {
        this.modelMapper = new ModelMapper();
    }

}
