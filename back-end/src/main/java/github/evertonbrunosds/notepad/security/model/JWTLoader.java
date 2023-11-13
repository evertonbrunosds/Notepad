package github.evertonbrunosds.notepad.security.model;

import java.util.Optional;
import java.util.UUID;

import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTLoader {

    private final byte[] key;

    public Optional<UUID> loadIdUserprofilePkFromToken(final String token) {
        try {
            final var parser = Jwts.parserBuilder().setSigningKey(key).build();
            final var claims = parser.parseClaimsJws(token).getBody();
            final var payload = UUID.fromString(claims.getSubject());
            return Optional.ofNullable(payload);
        } catch (final Throwable throwable) {
            return Optional.empty();
        }
    }

    public Optional<String> loadTokenFromRequest(final HttpServletRequest request) {
        final var token = request.getHeader("Authorization");
        return StringUtils.hasText(token)
                ? Optional.ofNullable(token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token)
                : Optional.empty();
    }

    public Optional<UUID> loadIdUserprofilePkFromRequest(final HttpServletRequest request) {
        final var token = loadTokenFromRequest(request);
        return token.isPresent()
                ? loadIdUserprofilePkFromToken(token.get())
                : Optional.empty();
    }

}
