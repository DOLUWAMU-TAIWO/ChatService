
package dev.dolu.chat_service.Secuirty;


import org.springframework.beans.factory.annotation.Autowired;
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



    // BCrypt bean for password hashing
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main security configuration, now using JWT authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity; enable for production
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set to stateless for JWT usage
                )
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(
                                "/error",
                                "/api/users/register",
                                "/api/users/login",
                                "/api/messages",
                                "/api/users/verify",
                                "/api/messages/{chatId}",
                                "/api/chats",
                                "/api/users"
                        ).permitAll() // Publicly accessible endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restricted to ADMIN role
                        .anyRequest().authenticated() // Other requests require authentication
                );
        return http.build();
    }

    // Define JwtAuthenticationFilter as a bean, passing in both jwtUtils and customUserDetailsService

}