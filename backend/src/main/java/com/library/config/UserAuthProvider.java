package com.library.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.library.dtos.UserDto;
import com.library.exceptions.AppException;
import com.library.mappers.UserMapper;
import com.library.model.User;
import com.library.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import java.lang.String;


import java.util.Base64;
import java.util.Collections;
import java.util.Date;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${security.jwt.token. secret-key: secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        //this is to avoid having the row secret key available in the JWM
        //only the backend can encode and decode the JWT
        //=> the need of the secret key
    }
    //I need 2 methods, one to generate the token, and one to validate this
    public String createToken(UserDto dto){
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);
        //this means that we will wait 1h until the authentication finishes
        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName",dto.getFirstName())
                .withClaim("lastName", dto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }
    public Authentication validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto user;
        user = UserDto.builder()
                .login(decoded.getIssuer())
                .firstName(decoded.getClaim("firstName").asString())
                .lastName(decoded.getClaim("lastName").toString())
                .build();

        //i use the information in the JWT to create a user DTO
        //with the login, the first name and the last name
        return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());
        //let's use the first method in the controller to return the new token
        // we are moving in the AuthController class
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        //the stronger validation starts as before, but i will
        //also check in the repository the existance of the user

        User user = userRepository.findByLogin(decoded.getIssuer())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        //but i will also check in the repo the existance of the user
        return new UsernamePasswordAuthenticationToken(userMapper.toUserDto(user),null, Collections.emptyList());
        //as before, return an authentification Bean
    }
}