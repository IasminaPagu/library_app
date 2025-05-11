package com.library.Controllers;

import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
//        List<Book> results = bookRepository.findByTitleContaining(title);
//        return ResponseEntity.ok(results);
//    }
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam("title") String title) {
        List<Book> results = bookRepository.findByTitleContaining(title);
        return ResponseEntity.ok(results);
    }

}
