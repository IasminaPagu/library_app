package com.library.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.library.services.WishlistService;
import com.library.dtos.AddToWishlistRequest;
import com.library.dtos.WishlistItemDTO;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody AddToWishlistRequest request) {
        boolean added = wishlistService.addToWishlist(request.getUserId(), request.getBookId());
        if (!added) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book already in wishlist");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<WishlistItemDTO> getWishlist(@RequestParam Long userId) {
        return wishlistService.getWishlistItems(userId);
    }
}
