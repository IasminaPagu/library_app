package com.library.Controllers;

import com.library.dtos.AddToCartRequest;
import com.library.dtos.CartDto;
import com.library.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import com.library.dtos.UserDto;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDto> addToCart(
            @AuthenticationPrincipal UserDto userDto,
            @RequestBody AddToCartRequest request) {
        CartDto cart = cartService.addToCart(userDto.getId(), request.bookId(), request.quantity());
        System.out.println("➡️ userDto: " + userDto);
        return ResponseEntity.ok(cart);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        System.out.println("📌 principal: " + authentication.getPrincipal());

        if (!(authentication.getPrincipal() instanceof UserDto userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CartDto cart = cartService.getCart(userDto.getId());
        return ResponseEntity.ok(cart);
    }
}