package tech.carrotly.restapi.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import tech.carrotly.restapi.model.response.UserResponse;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimResolver);
    String generateToken(UserResponse userDetails);
    String generateToken(Map<String, Object> extraClaims, UserResponse userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
