package io.github.speranskyartyom.taxi_microservices.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class InternalApiKeyFilter extends OncePerRequestFilter {
    private final String expectedKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String actualKey = request.getHeader("X-Internal-Service-Key");
        if (expectedKey.equals(actualKey)) {
            var auth = new UsernamePasswordAuthenticationToken("internal", null,
                    List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}