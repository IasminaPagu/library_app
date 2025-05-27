package com.library.dtos; // or DTOs if that's your package

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddToWishlistRequest {
    private Long bookId;
}