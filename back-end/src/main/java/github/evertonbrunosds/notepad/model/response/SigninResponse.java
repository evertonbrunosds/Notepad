package github.evertonbrunosds.notepad.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninResponse {

    private UUID idUserprofilePk;

    private String username;

    private LocalDate birthday;

    private String email;

    private LocalDateTime created;

}
