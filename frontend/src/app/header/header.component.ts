import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookService, Book } from '../services/book.service';
import { BookCardComponent } from '../components/book-card/book-card.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';


@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [FormsModule, CommonModule, RouterModule, BookCardComponent]
})
export class HeaderComponent {
  @Input() pageTitle = '';
  @Input() logoSrc = '';
  @Output() loginEvent = new EventEmitter<void>();
  @Output() logoutEvent = new EventEmitter<void>();

  searchTerm = '';
  books: Book[] = [];
  errorMessage = '';

  constructor(
    private bookService: BookService,
    private router: Router
  ) {}


  // onSearch() {
  //   if (!this.searchTerm.trim()) return;
  //
  //   this.bookService.searchBooks(this.searchTerm).subscribe({
  //     next: (results) => {
  //       this.books = results;
  //       this.errorMessage = results.length === 0 ? 'Cartea nu a fost găsită.' : '';
  //     },
  //     error: () => {
  //       this.books = [];
  //       this.errorMessage = 'Eroare la conectarea cu baza de date.';
  //     }
  //   });
  // }
  onSearch() {
    if (!this.searchTerm.trim()) return;

    this.bookService.searchBooks(this.searchTerm).subscribe({
      next: (results) => {
        if (results.length > 0) {
          const book = results[0]; // presupunem prima carte găsită
          this.router.navigate(['/books', book.id]);
        } else {
          this.errorMessage = 'Cartea nu a fost găsită.';
        }
      },
      error: () => {
        this.errorMessage = 'Eroare la conectarea cu baza de date.';
      }
    });
  }

  getBookWithImage(book: Book): Book {
    return {
      ...book,
      imageUrl: `https://via.placeholder.com/150?text=${book.title}`
    };
  }

}
