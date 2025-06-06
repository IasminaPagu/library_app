package com.library.repository;

import com.library.model.Wishlist;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserId(Long userId);
}