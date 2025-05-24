package com.library.Controllers;

import com.library.dtos.AddToCartRequest;
import com.library.dtos.CartDto;
import com.library.model.User;
import com.library.services.CartService;
import com.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.library.dtos.UserDto;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

//    @PostMapping("/add")
//    public ResponseEntity<CartDto> addToCart(
//            @AuthenticationPrincipal UserDto userDto,
//            @RequestBody AddToCartRequest request) {
//        CartDto cart = cartService.addToCart(userDto.getId(), request.bookId(), request.quantity());
//        return ResponseEntity.ok(cart);
//    }

    @PostMapping("/cart/add")
    public ResponseEntity<CartDto> addToCart(
            @AuthenticationPrincipal UserDto userDto,
            @RequestBody AddToCartRequest request
    ) {
        CartDto cart = cartService.addToCart(
                userDto.getId(),
                request.bookId(),
                request.quantity()
        );
        return ResponseEntity.ok(cart);
    }


    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserDto userDto) {
        CartDto cart = cartService.getCart(userDto.getId());
        return ResponseEntity.ok(cart);
    }
}
