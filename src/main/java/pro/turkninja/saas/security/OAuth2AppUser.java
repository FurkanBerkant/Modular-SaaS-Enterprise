package pro.turkninja.saas.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2AppUser implements OAuth2User {

    private final AppUser appUser;
    private final Map<String, Object> attributes;

    public OAuth2AppUser(AppUser appUser, Map<String, Object> attributes) {
        this.appUser = appUser;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name()));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return appUser.getEmail();
    }

}