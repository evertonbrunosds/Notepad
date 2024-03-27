package github.evertonbrunosds.notepad.security.configuration;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.OncePerRequestFilter;

import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.security.model.AllowedRoutes;
import github.evertonbrunosds.notepad.security.model.Author;
import github.evertonbrunosds.notepad.security.model.JWTLoader;
import github.evertonbrunosds.notepad.service.UserprofileService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class JWTFilterConfiguration extends OncePerRequestFilter {

    private final static Class<? extends Filter> BEFORE_FILTER = UsernamePasswordAuthenticationFilter.class;

    private static final String AUTHOR = "AUTHOR";

    private final JWTLoader jwtLoader;

    private final UserprofileService service;

    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response, final @NonNull FilterChain filterChain) throws ServletException, IOException {
        jwtLoader.loadIdUserprofilePkFromRequest(request).ifPresent((@NonNull UUID idUserprofilePk) -> {
            service.findByIdUserprofilePk(idUserprofilePk).ifPresent(userprofile -> {
                if (isValid(userprofile)) {
                    final var authentication = new UsernamePasswordAuthenticationToken(userprofile, emptyList(), userprofile.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute(AUTHOR, userprofile);
                }
            });
        });
        filterChain.doFilter(request, response);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors(content -> content.disable())
                .csrf(content -> content.disable())
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    });
                })
                .sessionManagement(content -> content.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(HttpMethod.OPTIONS, AllowedRoutes.options()).permitAll()
                            .requestMatchers(HttpMethod.DELETE, AllowedRoutes.delete()).permitAll()
                            .requestMatchers(HttpMethod.TRACE, AllowedRoutes.trace()).permitAll()
                            .requestMatchers(HttpMethod.PATCH, AllowedRoutes.patch()).permitAll()
                            .requestMatchers(HttpMethod.HEAD, AllowedRoutes.head()).permitAll()
                            .requestMatchers(HttpMethod.POST, AllowedRoutes.post()).permitAll()
                            .requestMatchers(HttpMethod.PUT, AllowedRoutes.put()).permitAll()
                            .requestMatchers(HttpMethod.GET, AllowedRoutes.get()).permitAll()
                            .anyRequest().authenticated();
                });
        http.addFilterBefore(this, BEFORE_FILTER);
        return http.build();
    }

    public boolean isValid(final UserDetails userDetails) {
        return (userDetails.isAccountNonExpired() &&
        userDetails.isAccountNonLocked() &&
        userDetails.isCredentialsNonExpired() &&
        userDetails.isEnabled()) == true;
    }

    @Bean
    @RequestScope
    public Author author(final HttpServletRequest request) {
        return new Author((UserprofileEntity) request.getAttribute(AUTHOR));
    }

}
