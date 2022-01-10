package dev.rmjr.todo.interceptor;

import dev.rmjr.todo.service.KeyService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final KeyService keyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ") && isTokenValid(token.substring(7))) {
            authenticate(token.substring(7));
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenEmail(token));
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities()));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(keyService.getKeyPair().getPublic())
                    .parseClaimsJws(token);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public String getTokenEmail(String token) {
        return Jwts.parser()
                .setSigningKey(keyService.getKeyPair().getPublic())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
