// package com.library.config;

// import com.library.dtos.UserDto;
// import com.library.model.User;
// import io.jsonwebtoken.*;
// import io.jsonwebtoken.security.Keys;
// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Component;

// import java.security.Key;
// import java.util.Date;
// import java.util.List;

// @Component
// @RequiredArgsConstructor
// public class UserAuthProvider {

//     private String secret = "verySecretKeyUsedForJWTGenerationMustBeLongEnough!";
//     private Key secretKey;

//     private final long validityInMilliseconds = 3600000; // 1h

//     @PostConstruct
//     protected void init() {
//         this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//     }

//     public String createToken(User user) {
//         Claims claims = Jwts.claims().setSubject(user.getLogin());
//         claims.put("id", user.getId());
//         claims.put("firstName", user.getFirstName());
//         claims.put("lastName", user.getLastName());

//         Date now = new Date();
//         Date validity = new Date(now.getTime() + validityInMilliseconds);

//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setIssuedAt(now)
//                 .setExpiration(validity)
//                 .signWith(secretKey)
//                 .compact();
//     }

//     public Authentication validateToken(String token) {
//         return new UsernamePasswordAuthenticationToken(
//                 getUserDtoFromToken(token),
//                 null,
//                 List.of());
//     }

//     public Authentication validateTokenStrongly(String token) {
//         return new UsernamePasswordAuthenticationToken(
//                 getUserDtoFromToken(token),
//                 null,
//                 List.of());
//     }

//     private UserDto getUserDtoFromToken(String token) {
//         Claims claims = Jwts.parserBuilder()
//                 .setSigningKey(secretKey)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();

//         return new UserDto(
//                 claims.get("id", Integer.class).longValue(), // sau Long.class, după caz
//                 claims.get("firstName", String.class),
//                 claims.get("lastName", String.class),
//                 claims.getSubject(), // login
//                 token);
//     }
// }

package com.library.config;

import com.library.dtos.UserDto;
import com.library.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKeyString;

    private SecretKey secretKey;

    private final long validityInMilliseconds = 3600000; // 1h

    @PostConstruct
    protected void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(secretKey, SignatureAlgorithm.HS384)
                .compact();
    }

    public Authentication validateToken(String token) {
        return new UsernamePasswordAuthenticationToken(
                getUserDtoFromToken(token),
                null,
                List.of());
    }

    public Authentication validateTokenStrongly(String token) {
        return new UsernamePasswordAuthenticationToken(
                getUserDtoFromToken(token),
                null,
                List.of());
    }

    private UserDto getUserDtoFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UserDto(
                claims.get("id", Integer.class).longValue(),
                claims.get("firstName", String.class),
                claims.get("lastName", String.class),
                claims.getSubject(), // login
                token);
    }
}
