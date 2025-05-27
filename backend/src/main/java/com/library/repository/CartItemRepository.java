// src/main/java/com/library/repository/WishlistItemRepository.java
package com.library.repository;

import com.library.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
