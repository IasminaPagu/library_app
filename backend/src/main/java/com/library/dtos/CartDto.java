package com.library.dtos;

import java.util.List;

public record CartDto(List<CartItemDto> items) {}