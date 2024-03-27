package github.evertonbrunosds.notepad.util;

import static github.evertonbrunosds.notepad.util.Parameter.UNDETERMINED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourceException extends RuntimeException {

    private final HttpStatus involvedStatus;

    private final Parameter involvedParameter;

    private final Class<?> involvedClass;

    @NonNull
    public HttpStatus getInvolvedStatus() {
        return involvedStatus != null
                ? involvedStatus
                : INTERNAL_SERVER_ERROR;
    }

    public Parameter getInvolvedParameter() {
        return involvedParameter != null
                ? involvedParameter
                : UNDETERMINED;
    }

    public Class<?> getInvolvedClass() {
        return involvedClass != null
                ? involvedClass
                : UNDETERMINED.getClass();
    }

    public Reason getReason() {
        return new Reason();
    }

    public final class Reason {

        private Reason() {}

        public String getStatusName() {
            return toString(getInvolvedStatus());
        }

        public String getStatusCode() {
            return toString(getInvolvedStatus().value());
        }

        public String getStatusParameter() {
            return getInvolvedParameter().toString();
        }

        public String getStatusClass() {
            return toString(involvedClass);
        }

        private String toString(final HttpStatus involvedStatus) {
            final String space = " ";
            final String underline = "_";
            return involvedStatus.getReasonPhrase().toUpperCase().replace(space, underline);
        }

        private String toString(final Integer involvedValue) {
            return involvedValue.toString();
        }

        private String toString(final Class<?> involvedClass) {
            return involvedClass.getName();
        }

    }

}