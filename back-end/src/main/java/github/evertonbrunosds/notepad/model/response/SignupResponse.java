package github.evertonbrunosds.notepad.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponse {

    private String username;

    private LocalDate birthday;

    private String email;

    private LocalDateTime created;

}
