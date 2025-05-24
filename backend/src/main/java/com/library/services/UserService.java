package com.library.services;

import com.library.dtos.CredentialsDto;
import com.library.dtos.UserDto;
import com.library.exceptions.AppException;
import com.library.mappers.UserMapper;
import com.library.model.User;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.library.dtos.SignUpDto;
import java.util.Optional;


import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor

public class UserService {
    // i start by adding all the dependencies in my repository
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserDto login(CredentialsDto creds) {
        User user = userRepository.findByLogin(creds.login())
                .orElseThrow(() -> new UsernameNotFoundException("No user " + creds.login()));
        if (!passwordEncoder.matches(creds.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }
        // map entity → DTO (will include id)
        return userMapper.toUserDto(user);
    }

    /** Used by CartController to look up the full UserDto by name in the JWT. */
    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("No user " + login));
        return userMapper.toUserDto(user);
    }

    // … register(...) similarly maps and then dto.setLogin(...)

//    public UserDto register(SignUpDto signUpDto){
//        Optional<User> oUser = userRepository.findByLogin(signUpDto.login());
//        //if the user is already stored in the database i throw a new exception
//        if(oUser.isPresent()){
//            throw new AppException("Login already exists",HttpStatus.BAD_REQUEST);
//        }
//        //otherwise, i map the DTO into the entity
//        User user = userMapper.signUpToUser(signUpDto);
//        //and i encrypt/encode the password
//        user.setPassword(passwordEncoder.encode(signUpDto.password()));
//        //and now i save the entity
//        User savedUser = userRepository.save(user);
//
//
//        return userMapper.toUserDto(savedUser);
//    }
    public UserDto register(SignUpDto signUpDto){
        Optional<User> oUser = userRepository.findByLogin(signUpDto.login());
        if(oUser.isPresent()){
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        // ✅ Manually set each field (this guarantees it's not null)
        User user = new User();
        user.setFirstName(signUpDto.firstName());
        user.setLastName(signUpDto.lastName());
        user.setLogin(signUpDto.login());
        user.setPassword(passwordEncoder.encode(signUpDto.password()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }


}