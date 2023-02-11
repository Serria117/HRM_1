package com.hrm.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component @Slf4j
public class JWTProvider
{
    @Value("${jwt.secret}")
    String secret;
    long JWT_LIFE_TIME = 5 * 60 * 60 * 1000;
    long REFRESH_TOKEN_LIFE_TIME = 10 * 60 * 60 * 1000;

    private Key getSigningKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Collection<String> getAuthorityFromUser(UserDetails userDetails)
    {
        return userDetails.getAuthorities().stream()
                          .map(GrantedAuthority::getAuthority)
                          .filter(a -> !a.startsWith("ROLE"))
                          .collect(Collectors.toList());
    }

    private Collection<String> getRoleFromUser(UserDetails userDetails)
    {
        return userDetails.getAuthorities().stream()
                          .map(GrantedAuthority::getAuthority)
                          .collect(Collectors.toList());
    }

    private <T> T extractFromToken(String token, Function<Claims, T> claimResolver)
    {
        var claims = Jwts.parserBuilder()
                         .setSigningKey(this.getSigningKey())
                         .build()
                         .parseClaimsJws(token)
                         .getBody();
        return claimResolver.apply(claims);
    }

    public String createToken(UserDetails userDetails)
    {
        var claims = new HashMap<String, Object>();
        claims.put("roles", getRoleFromUser(userDetails));
        claims.put("authority", getAuthorityFromUser(userDetails));
        var now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + JWT_LIFE_TIME))
                   .signWith(getSigningKey())
                   .compact();
    }

    public String getUsernameFromToken(String token)
    {
        return extractFromToken(token, Claims::getSubject);
    }

    public Date getExpiration(String token)
    {
        return extractFromToken(token, Claims::getExpiration);
    }

    public List<?> getRoles(String token)
    {
        return extractFromToken(token, claim -> claim.get("roles", ArrayList.class));
    }

    public boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        catch ( ExpiredJwtException e ) {
            log.error("Token has been expired. " + e.getMessage());
        }
        catch ( UnsupportedJwtException e ) {
            log.error("Jwt format not supported. " + e.getMessage());
        }
        catch ( MalformedJwtException | IllegalArgumentException e ) {
            log.error(e.getMessage());
        }
        catch ( SignatureException e ) {
            log.error("Invalid signature. " + e.getMessage());
        }
        return false;
    }
}
