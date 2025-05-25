package com.library.services;

import com.library.dtos.CredentialsDto;
import com.library.dtos.UserDto;
import com.library.exceptions.AppException;
import com.library.mappers.UserMapper;
import com.library.model.User;
import com.library.config.UserAuthProvider;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.library.dtos.SignUpDto;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // i start by adding all the dependencies in my repository
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserAuthProvider authProvider;

    public UserDto login(CredentialsDto creds) {
    User user = userRepository.findByLogin(creds.getLogin())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

    if (!passwordEncoder.matches(creds.getPassword(), user.getPassword())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), authProvider.createToken(user));
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