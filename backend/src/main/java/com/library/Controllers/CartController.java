package com.library.Controllers;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.Book;
import com.library.model.CartItem;
import com.library.repository.BookRepository;
import com.library.services.CartService;

import lombok.RequiredArgsConstructor;

import com.library.dtos.AddToCartRequest;
import com.library.dtos.CartDto;
import com.library.dtos.UpdateCartItemRequest;
import com.library.dtos.UserDto;
import com.library.dtos.CheckoutRequest;
import org.springframework.security.core.Authentication;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookRepository bookRepository;


    // @PostMapping("/add")
    // public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
    //     UserDto userDto = (UserDto) authentication.getPrincipal();
    //     cartService.addToCart(userDto.getId(), request.getBookId(), 1);
    //     return ResponseEntity.ok().build();
    // }
    // @PostMapping("/add")
    // public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
    //     UserDto userDto = (UserDto) authentication.getPrincipal();

    //     // 1. Obține cartea
    //     Book book = bookRepository.findById(request.getBookId().intValue())
    //             .orElseThrow(() -> new RuntimeException("Cartea nu a fost găsită."));

    //     // 2. Verifică stocul
    //     if (book.getStock() <= 0) {
    //         return ResponseEntity.badRequest()
    //                 .body(Map.of("message", "Stoc insuficient, vă rugăm selectați o altă carte."));
    //     }

    //     // 3. Dacă totul e ok, adaugă în coș
    //     cartService.addToCart(userDto.getId(), request.getBookId(), 1);
    //     return ResponseEntity.ok().build();
    // }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        Book book = bookRepository.findById(request.getBookId().intValue())
                .orElseThrow(() -> new RuntimeException("Cartea nu a fost găsită."));

        if (book.getStock() == null || book.getStock() <= 0) {
            System.out.println("📛 Stoc insuficient pentru: " + book.getTitle() + ", stock=" + book.getStock());

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Stoc insuficient, vă rugăm selectați o altă carte."));
        }

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
    @PostMapping("/checkout")
    public ResponseEntity<?> finalizeOrder(@RequestBody CheckoutRequest request,Authentication authentication) {
    UserDto userDto = (UserDto) authentication.getPrincipal();

    for (UpdateCartItemRequest item : request.getItems()) {
        Book book = bookRepository.findById(item.getBookId().intValue())
            .orElseThrow(() -> new RuntimeException("Carte inexistentă"));

        if (book.getStock() < item.getQuantity()) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Stoc insuficient pentru cartea: " + book.getTitle()
            ));
        }

        book.setStock(book.getStock() - item.getQuantity());
        bookRepository.save(book);
    }

    cartService.clearCart(userDto.getId());
    return ResponseEntity.ok(Map.of("message", "Comanda a fost finalizată cu succes"));
}
}