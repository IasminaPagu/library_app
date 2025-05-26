package com.library.services;

import com.library.dtos.CartDto;
import com.library.dtos.CartItemDto;
import com.library.exceptions.AppException;
import com.library.model.Book;
import com.library.model.Cart;
import com.library.model.CartItem;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.CartRepository;
import com.library.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

        private final CartRepository cartRepository;
        private final BookRepository bookRepository;
        private final UserRepository userRepository;

        public CartService(CartRepository cartRepository,
                        BookRepository bookRepository,
                        UserRepository userRepository) {
                this.cartRepository = cartRepository;
                this.bookRepository = bookRepository;
                this.userRepository = userRepository;
        }

        /**
         * Adaugă o carte în coș (sau crește cantitatea dacă există deja), doar dacă
         * există suficient stoc.
         */
        @Transactional
        public CartDto addToCart(Long userId, Long bookId, int qty) {
                // 1. Încarcă user-ul
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

                // 2. Ia (sau creează) coșul
                Cart cart = cartRepository.findByUser(user)
                                .orElseGet(() -> {
                                        Cart c = new Cart();
                                        c.setUser(user);
                                        return cartRepository.save(c);
                                });

                // 3. Încarcă cartea
                Book book = bookRepository.findById(Math.toIntExact(bookId))
                                .orElseThrow(() -> new AppException("Book not found", HttpStatus.NOT_FOUND));

                // 4. Verifică dacă stocul este suficient
                if (book.getStock() < qty) {
                        throw new AppException("Cantitatea solicitată depășește stocul disponibil.",
                                        HttpStatus.BAD_REQUEST);
                }

                // 5. Caută item-ul existent în coș
                Optional<CartItem> existingOpt = cart.getItems().stream()
                                .filter(i -> Objects.equals(i.getBook().getId(), bookId))
                                .findFirst();

                if (existingOpt.isPresent()) {
                        // 6a. Dacă există deja, crește cantitatea
                        CartItem existing = existingOpt.get();
                        existing.setQuantity(existing.getQuantity() + qty);
                } else {
                        // 6b. Dacă nu există, creează un nou CartItem
                        CartItem item = new CartItem();
                        item.setBook(book);
                        item.setCart(cart);
                        item.setQuantity(qty);
                        cart.getItems().add(item);
                }

                // 7. Salvează și returnează DTO-ul
                Cart saved = cartRepository.save(cart);
                return mapToDto(saved);
        }

        /**
         * Returnează coșul curent al user-ului.
         */
        public CartDto getCart(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

                Cart cart = cartRepository.findByUser(user)
                                .orElse(new Cart());
                return mapToDto(cart);
        }

        /**
         * Mapare internă Cart → CartDto.
         */
        private CartDto mapToDto(Cart cart) {
                return new CartDto(
                                cart.getItems().stream()
                                                .map(i -> new CartItemDto(
                                                                i.getBook().getId(),
                                                                i.getBook().getTitle(),
                                                                i.getQuantity()))
                                                .collect(Collectors.toList()));
        }
}
