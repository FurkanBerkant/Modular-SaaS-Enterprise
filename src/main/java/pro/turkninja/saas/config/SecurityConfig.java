package pro.turkninja.saas.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pro.turkninja.saas.tenant.TenantFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                "/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/uploads/**", "/webjars/**"
                        ).permitAll()

                        // Provider sayfaları
                        .requestMatchers("/dashboard", "/provider/**").hasRole("PROVIDER")

                        // Employee sayfaları
                        .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "PROVIDER")

                        // Customer sayfaları
                        .requestMatchers("/booking", "/request").hasAnyRole("CUSTOMER", "PROVIDER")

                        // Onboarding: giriş yapmış herkes erişebilir (rol CUSTOMER → PROVIDER geçişi burada)
                        .requestMatchers("/provider/onboarding").authenticated()

                        // API'ler: authenticated, method-level @PreAuthorize ile korunuyor
                        .requestMatchers("/api/**").authenticated()

                        // Admin
                        .requestMatchers("/admin/**").hasRole("SUPER_ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(roleBasedSuccessHandler())
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(roleBasedSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // CSRF aktif — HTML form'lar th:action ile otomatik token alır,
                // fetch istekleri th:data-csrf-token ile header'a ekler.
                .addFilterAfter(new TenantFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (HttpServletRequest req, HttpServletResponse res, Authentication auth) -> {
            String redirectUrl;
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROVIDER"))) {
                redirectUrl = "/dashboard";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))) {
                redirectUrl = "/employee/dashboard";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
                redirectUrl = "/booking";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
                redirectUrl = "/admin";
            } else {
                redirectUrl = "/";
            }
            res.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}