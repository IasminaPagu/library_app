package com.library.Controllers;

import com.library.config.UserAuthProvider;
import com.library.dtos.CredentialsDto;
import com.library.dtos.UserDto;
import com.library.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.library.dtos.SignUpDto;
import java.net.URI;



@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        log.info("🎯 after login(), UserDto = {}", user);
        // or if you just want the login field:
        log.info("🎯 after login(), user.getLogin() = {}", user.getLogin());
        //this is the point i return after the the modifications made in
        //UserAuthProvider.java for the JWT
        user.setToken(userAuthProvider.createToken(user));
        log.info("AFTER LOGIN – UserDto = {}", user);
        return ResponseEntity.ok(user);
    //i will create the CredentialsDto as a record
    // and UserDto as a POJO - Plain Old Java Object


    //why using a class or record ?
    //because the credentials DTO it s just for reception
    // i won t edit the content field by field
    //and for the UserDto i would use it for the reception and for the transmission
    //and not all the fields will be set together
    // !! the records are immutable
    }
    @PostMapping("/register") // i add a new endpoint, register
    //this method will return a UserDto as before, but as an input i will have a
    //signup dto, a new dto, which i will create later
    public ResponseEntity<UserDto> register (@RequestBody SignUpDto signUpDto){
        //i call the register method from the service, which i will also create later
        System.out.println("✅ Register endpoint HIT: " + signUpDto.login());
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        //i will return a created response, which contains the URL of the created entity
        //and the body
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }
}
