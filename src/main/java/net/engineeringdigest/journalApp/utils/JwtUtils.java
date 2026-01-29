package net.engineeringdigest.journalApp.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.awt.peer.SystemTrayPeer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private String SECRET_KEY = "Pe+3r74Is4OIc@d0^cs#ko$#@krR9fjKAfj9e#E@q2k3#@$5^K$@K";

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token){
        return !isTokenExpired(token);
    }

    public String generateToken(String username,int expiry) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username,expiry);
    }

    public String createToken(Map<String,Object>  claims,String subject,int expiry){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","jwt")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiry))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }
}
