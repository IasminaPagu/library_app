// src/main/java/com/library/dtos/UpdateCartItemRequest.java
package com.library.dtos;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long bookId;
    private int quantity;

}
