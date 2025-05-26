package com.library.Controllers;

import com.library.dtos.AddToCartRequest;
import com.library.dtos.CartDto;
import com.library.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.library.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        cartService.addToCart(userDto.getId(), request.getBookId(), 1);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        System.out.println("principal: " + authentication.getPrincipal());

        if (!(authentication.getPrincipal() instanceof UserDto userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartDto cart = cartService.getCart(userDto.getId());
        return ResponseEntity.ok(cart);
    }
}