package com.library.services;

import com.library.dtos.CredentialsDto;
import com.library.dtos.UserDto;
import com.library.exceptions.AppException;
import com.library.mappers.UserMapper;
import com.library.model.User;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        //and now i compare the given password with the password stored in the db
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword())) {
            return userMapper.toUserDto(user);
        //if it s correct, i map the user from the database to a user DTO

        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
        //and if not, i throw another exception

    }
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