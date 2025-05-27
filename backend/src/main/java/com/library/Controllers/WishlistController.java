package com.library.Controllers;

import com.library.dtos.AddToWishlistRequest;
import com.library.dtos.MoveToCartRequest;
import com.library.dtos.WishlistDto;
import com.library.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.library.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody AddToWishlistRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        wishlistService.addToWishlist(userDto.getId(), request.getBookId(), 1);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<WishlistDto> getWishlist(Authentication authentication) {
        System.out.println("principal: " + authentication.getPrincipal());

        if (!(authentication.getPrincipal() instanceof UserDto userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        WishlistDto wishlist = wishlistService.getWishlist(userDto.getId());
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(@AuthenticationPrincipal UserDto user,
                                                @RequestParam("bookId") Long bookId) {
        boolean removed = wishlistService.removeFromWishlist(user.getId(), bookId);
        if (!removed) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Item not in wishlist");
        }
        return ResponseEntity.ok().build();
    }
    // în WishlistController

    @PostMapping("/move-to-cart")
    public ResponseEntity<?> moveToCart(
            @AuthenticationPrincipal UserDto user,
            @RequestBody MoveToCartRequest req
    ) {
        wishlistService.moveAllToCart(user.getId(), req.getBookId());
        return ResponseEntity.ok().build();
    }

}


