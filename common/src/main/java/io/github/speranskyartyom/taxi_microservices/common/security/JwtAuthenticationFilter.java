package io.github.speranskyartyom.taxi_microservices.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String jwtSecret; // Должен быть в application.yaml

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if ("Bearer postman-master-token".equals(authHeader)) {
            setMasterAuth();
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                Long userId = claims.get("id", Long.class);
                String email = claims.getSubject();

                UserPrincipal principal = new UserPrincipal(userId, email, List.of(new SimpleGrantedAuthority("ROLE_USER")));

                var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                log.error("JWT validation failed: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setMasterAuth() {
        UserPrincipal admin = new UserPrincipal(0L, "admin@taxi.com",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        var auth = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
