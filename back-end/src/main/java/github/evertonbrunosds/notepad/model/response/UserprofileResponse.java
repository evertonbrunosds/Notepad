package github.evertonbrunosds.notepad.model.response;

import static github.evertonbrunosds.notepad.util.LocalDateManager.getYears;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserprofileResponse {

    @Getter(onMethod_ = @NonNull)
    private UUID idUserprofilePk;

    private String username;

    private LocalDate birthday;

    private LocalDateTime created;

    public int getAge() {
        return getYears(birthday);
    }
    
}
