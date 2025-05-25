package com.library.dtos; // or DTOs if that's your package

public class AddToCartRequest {
    private Long bookId;

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}