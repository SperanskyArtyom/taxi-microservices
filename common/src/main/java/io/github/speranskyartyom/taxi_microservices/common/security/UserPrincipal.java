package io.github.speranskyartyom.taxi_microservices.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class UserPrincipal {
    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
}
