package github.evertonbrunosds.notepad.security.model;

import java.util.function.Supplier;

public interface Access<O> {

    public <T extends Throwable> O orThrow(final Supplier<T> supplier) throws T;

}
