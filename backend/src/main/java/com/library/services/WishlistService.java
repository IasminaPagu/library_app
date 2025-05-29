package com.library.services;

import com.library.dtos.WishlistDto;
import com.library.dtos.WishlistItemDTO;
import com.library.exceptions.AppException;
import com.library.model.*;
import com.library.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository itemRepo;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository, WishlistItemRepository itemRepo,CartRepository cartRepository,
                           CartItemRepository cartItemRepository) {
        this.wishlistRepository = wishlistRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.itemRepo = itemRepo;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
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

    @Transactional
    public boolean removeFromWishlist(Long userId, Long bookId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException("No wishlist for user " + userId, HttpStatus.NOT_FOUND));

        Optional<WishlistItem> opt = wishlist.getItems().stream()
                            // book.getId() is an Integer, bookId is a Long — compare as ints:
                            .filter(i -> i.getBook().getId().equals(bookId.intValue()))
                            .findFirst();

        if (opt.isEmpty()) {
            return false;
        }

        WishlistItem item = opt.get();
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            itemRepo.save(item);
        } else {
            itemRepo.delete(item);
        }
        return true;
    }

    // în WishlistService
    @Transactional
    public void moveAllToCart(Long userId, Long bookId) {
        // 1) Încarcă Wishlist & WishlistItems
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException("Wishlist inexistent", HttpStatus.NOT_FOUND));

        List<WishlistItem> items = wishlist.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId.intValue()))
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new AppException("Cartea nu este în wishlist", HttpStatus.NOT_FOUND);
        }

        // 2) Calculează total qty
        int totalQty = items.stream().mapToInt(WishlistItem::getQuantity).sum();

        // 3) Şterge din colecţia entităţii şi salvează Wishlist-ul
        wishlist.getItems().removeAll(items);
        wishlistRepository.save(wishlist);

        // 4) Apoi şterge efectiv înregistrările din tabel
        itemRepo.deleteAll(items);

        // 5) Încarcă/crează cart & CartItem
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User inexistent", HttpStatus.NOT_FOUND));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });

        CartItem cartItem = cart.getItems().stream()
                .filter(ci -> ci.getBook().getId().equals(bookId.intValue()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setBook(bookRepository.findById(Math.toIntExact(bookId))
                            .orElseThrow(() -> new AppException("Book not found", HttpStatus.NOT_FOUND)));
                    ci.setCart(cart);
                    ci.setQuantity(0);
                    cart.getItems().add(ci);
                    return ci;
                });

        cartItem.setQuantity(cartItem.getQuantity() + totalQty);
        cartItemRepository.save(cartItem);
    }

}

