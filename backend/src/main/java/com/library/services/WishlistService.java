package com.library.services;

import com.library.dtos.WishlistDto;
import com.library.dtos.WishlistItemDTO;
import com.library.exceptions.AppException;
import com.library.model.Book;
import com.library.model.Wishlist;
import com.library.model.WishlistItem;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.WishlistRepository;
import com.library.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adaugă o carte în wishlist (sau crește cantitatea dacă există deja), doar dacă
     * există suficient stoc.
     */
    @Transactional
    public WishlistDto addToWishlist(Long userId, Long bookId, int qty) {
        // 1. Încarcă user-ul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        // 2. Ia (sau creează) coșul
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUser(user);
                    return wishlistRepository.save(w);
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
        Optional<WishlistItem> existingOpt = wishlist.getItems().stream()
                .filter(i -> Objects.equals(i.getBook().getId(), bookId))
                .findFirst();

        if (existingOpt.isPresent()) {
            // 6a. Dacă există deja, crește cantitatea
            WishlistItem existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + qty);
        } else {
            // 6b. Dacă nu există, creează un nou CartItem
            WishlistItem item = new WishlistItem();
            item.setBook(book);
            item.setWishlist(wishlist);
            item.setQuantity(qty);
            wishlist.getItems().add(item);
        }

        // 7. Salvează și returnează DTO-ul
        Wishlist saved = wishlistRepository.save(wishlist);
        return mapToDto(saved);
    }

    /**
     * Returnează coșul curent al user-ului.
     */
    public WishlistDto getWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElse(new Wishlist());
        return mapToDto(wishlist);
    }

    /**
     * Mapare internă Wishlist → WishlistDto.
     */
    private WishlistDto mapToDto(Wishlist wishlist) {
        return new WishlistDto(
                wishlist.getItems().stream()
                        .map(i -> new WishlistItemDTO(
                                i.getBook().getId(),
                                i.getBook().getTitle(),
                                i.getQuantity()))
                        .collect(Collectors.toList()));
    }
}
