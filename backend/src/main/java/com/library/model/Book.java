package com.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
    private Integer stock;
}
