package com.library.Controllers;

import com.library.dtos.CredentialsDto;
import com.library.dtos.UserDto;
import com.library.dtos.SignUpDto;
import com.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto authRequest) {
        UserDto user = userService.login(authRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = userService.register(signUpDto);
        return ResponseEntity.ok(userDto);
    }
}
