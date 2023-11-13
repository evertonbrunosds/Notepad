package github.evertonbrunosds.notepad.model.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserprofileRequest {

    private String username;

    private LocalDate birthday;

    private String email;

    private String password;
    
}
