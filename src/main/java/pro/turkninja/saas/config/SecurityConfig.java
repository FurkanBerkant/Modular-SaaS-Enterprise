package pro.turkninja.saas.config;

import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import pro.turkninja.saas.security.CustomOAuth2UserService;
import pro.turkninja.saas.tenant.TenantFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // Onboarding: giriş yapmış herkes erişebilir (CUSTOMER → PROVIDER dönüşümü için)
                        .requestMatchers("/provider/onboarding").authenticated()
                        .requestMatchers("/dashboard", "/provider/**").hasRole("PROVIDER")
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                        .requestMatchers("/booking", "/request").hasRole("CUSTOMER")
                        .requestMatchers("/api/**").authenticated()
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
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(roleBasedSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
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
                // Müşteri giriş yapınca onboarding'e yönlendir
                // (İşletme sahibi olmak istiyorsa oradan devam eder)
                redirectUrl = "/provider/onboarding";
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