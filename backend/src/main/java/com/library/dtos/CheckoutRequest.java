package com.library.dtos;

import java.util.List;
import lombok.Data;

@Data
public class CheckoutRequest {
    private List<UpdateCartItemRequest> items;
}
