package com.fullApp.myApp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "QeSN2JMsJkyRzC/GBWtVVXo+UMy4Wk3xORrRVnHg9KfJqkLUcGdRsJNA+lx8vAp6oHYMwMOeBrJHsaUWTwt1r/UyzUVgMz+1gq9m5IUjeQ6uMz2tADCkptejTA1pzs9uMRie1IGt+RYZOBvkn8c9SBNfqWnpO9c7WA+Rx+af+cL6/pfnQY8Tij2i113iun5o0qRA898UPhdszhtU9zmMiigo9Cfl7zySHVhrLnHPbobcB8UeCtuSz4JwgpIgqPypURW2TscCNVL+QdrSlUugbnQV9zmQO9Ll/TXI7/8eyXJVOuQp9P1ehc6MMOSAZQVnUXLSZNETAeRSWQca5LjzAKrXmZ76m/PBIcVFvdrPLm8=";

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today =  Date.from(Instant.now());

        return getClaims(jwt).getExpiration().before(today);
    }

    public String issueToken(String subject){
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, List<String> scopes){
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, String ...scopes){
        return issueToken(subject, Map.of("scopes", scopes));
    }
    public String issueToken(
            String subject,
            Map<String, Object> claims
    ){
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("Nikitos")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(15, ChronoUnit.DAYS)
                        )
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }


    private Claims getClaims(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
    public String getSubject(String token){
         return getClaims(token).getSubject();
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


}
