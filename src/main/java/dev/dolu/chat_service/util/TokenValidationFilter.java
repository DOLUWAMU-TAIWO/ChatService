package dev.dolu.chat_service.util;

import dev.dolu.chat_service.Service.RestUserServiceClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final RestUserServiceClient userServiceClient;

    public TokenValidationFilter(RestUserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or invalid Authorization header.");
            return;
        }

        String token = authHeader.substring(7); // Extract token
        try {
            // Call the User Service to validate the token
            boolean isValid = userServiceClient.validateToken(token);
            if (!isValid) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired token.");
                return;
            }

            // Populate SecurityContext with a placeholder Authentication object
            // This ensures Spring Security recognizes the user as authenticated
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "authenticatedUser", // Placeholder username (optional, for logging/debugging)
                    null, // No credentials required
                    List.of() // Empty authorities since the Chat Service doesn't enforce roles
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Error validating token.");
            return;
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}