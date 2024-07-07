package com.vedha.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Data
@Service
//@PropertySources( @PropertySource("classpath:jwt-config.properties")) // For External Configuration File, it's only load .properties file for yml use it in application.yml (spring.config.import)
@ConfigurationProperties(prefix = "jwt")
public class JwtService {

    // https://asecuritysite.com/encryption/plain
    private String secretKey;

    private long expiration;

    private long refreshExpiration;

    // https://jwt.io/

    /**
     * JWT Token will contain Header, Payload, and Signature. This method will extract the payload(body) from the JWT Token.
     * Payload will contain the claims like issuer, expiration, subject, and the user details.
     */
    public String generateToken(UserDetails userDetails) {

        return generateTokenWithExtraClaims(Map.of("roles", userDetails.getAuthorities()), userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenWithExtraClaims(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {

        String userName = extractUserName(jwtToken);
        Date expirationDate = extractExpirationDate(jwtToken);

        return userName.equals(userDetails.getUsername()) && !expirationDate.before(new Date());
    }

    public String extractUserName(String jwtToken) {

        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Date extractExpirationDate(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public Date extractIssuedAt(String jwtToken) {

        return extractClaim(jwtToken, Claims::getIssuedAt);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {

        return claimsResolver.apply(extractAllClaims(jwtToken));
    }

    /**
     * JWT Token will contain Header, Payload, and Signature. This method will extract the payload(body) from the JWT Token.
     * Payload will contain the claims like issuer, expiration, subject, and the user details.
     */
    private Claims extractAllClaims(String jwtToken) {

        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    private Key getSigningKey() {

        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }
}
