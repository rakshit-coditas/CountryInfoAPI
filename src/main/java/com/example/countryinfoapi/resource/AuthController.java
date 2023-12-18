package com.example.countryinfoapi.resource;

import com.example.countryinfoapi.service.impl.CustomUserDetailsService;
import com.example.countryinfoapi.config.JwtTokenProvider;
import com.example.countryinfoapi.model.request.LoginRequest;
import com.example.countryinfoapi.model.response.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // Generate JWT token
        String token = jwtTokenProvider.createToken(request.getUsername());
        Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(token).getBody();
        Date expirationTime = claims.getExpiration();

        return ResponseEntity.ok(new JwtResponse(token,expirationTime));
    }
}


