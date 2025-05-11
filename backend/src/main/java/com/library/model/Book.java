package com.library.model;

import jakarta.persistence.*;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@Setter
@Getter
@Entity
@Table(name = "books") // Hibernate va crea o tabelă numită "books"
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
    private String description;
    private String imageUrl;
    private String category;

    public Book() {}

    public Book(String title, String author, String isbn, Integer publishedYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }


}
