package github.evertonbrunosds.notepad.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import github.evertonbrunosds.notepad.util.ResourceException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<?> handleResourceException(final ResourceException resourceException) {
        return new ResponseEntity<>(resourceException.getReason(), resourceException.getInvolvedStatus());
    }
    
}
