package dev.dolu.chat_service.Secuirty;

import dev.dolu.chat_service.util.TokenValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenValidationFilter tokenValidationFilter;

    public SecurityConfig(TokenValidationFilter tokenValidationFilter) {
        this.tokenValidationFilter = tokenValidationFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity; enable for production
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless for JWT
                )
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(
                                "/error",
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/verify"
                        ).permitAll() // Publicly accessible endpoints
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class); // Add TokenValidationFilter

        return http.build();
    }
}
