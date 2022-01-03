package dev.rmjr.todo.service;

import dev.rmjr.todo.request.UserLoginRequest;
import dev.rmjr.todo.response.TokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final AuthenticationManager authenticationManager;
    private final KeyService keyService;

    @Value("${jwt.expiration}")
    private String expiration;

    public TokenResponse generateUserToken(UserLoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return TokenResponse.builder()
                .type("Bearer")
                .token(generateToken(authentication))
                .build();
    }

    private String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("RMJR TODO")
                .setSubject(user.getUsername())
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.ES256, keyService.getKeyPair().getPrivate())
                .compact();
    }
}
