package com.library.repository;
import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // custom query to search to blog post by title or content
    List<Book> findByTitleContaining(String text);
}
