package com.library.Controllers;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final FileStorageService storage;

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam("title") String title) {
        List<Book> results = bookRepository.findByTitleContaining(title);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Integer id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> all = bookRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    @PreAuthorize(
            "principal.login == 'vlad' or " +
                    "principal.login == 'iasmina' or " +
                    "principal.login == 'gety'"
    )
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        if (book.getStock() == null) {
            book.setStock(1);
        }
        Book saved = bookRepository.save(book);
        // return 201 + Location header
        return ResponseEntity
                .created(URI.create("/books/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize(
            "principal.login == 'vlad' or " +
                    "principal.login == 'iasmina' or " +
                    "principal.login == 'gety'"
    )
    public ResponseEntity<Book> updateBook(
            @PathVariable("id") Integer id,
            @RequestBody Book book
    ) {
        return bookRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(book.getTitle());
                    existing.setAuthor(book.getAuthor());
                    existing.setDescription(book.getDescription());
                    existing.setIsbn(book.getIsbn());
                    existing.setStock(book.getStock());
                    existing.setImageUrl(book.getImageUrl());
                    existing.setPublishedYear(book.getPublishedYear());
                    existing.setCategory(book.getCategory());
                    existing.setStock(book.getStock());
                    Book updated = bookRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "principal.login == 'vlad' or " +
                    "principal.login == 'iasmina' or " +
                    "principal.login == 'gety'"
    )
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Integer id) {
        return bookRepository.findById(id)
                .map(b -> {
                    bookRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path="/{id}/image", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(
            "principal.login == 'vlad' or " +
                    "principal.login == 'iasmina' or " +
                    "principal.login == 'gety'"
    )
    public ResponseEntity<Void> uploadImage(
            @PathVariable("id") Integer id,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        // verifică existența cărţii
        bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // salvează sub numele {id}.jpg
        String filename = id + ".jpg";
        storage.store(file, filename);
        // actualizează câmpul imageUrl în DB
        Book book = bookRepository.getReferenceById(id);
        book.setImageUrl(filename);
        bookRepository.save(book);
        return ResponseEntity.ok().build();
    }
}
