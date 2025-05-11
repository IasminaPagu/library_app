// import { Component, EventEmitter, Input, Output } from '@angular/core';
// import { FormsModule } from '@angular/forms';
//
// @Component({
//   selector: 'app-header',
//   standalone: true,
//   imports: [FormsModule],
//   templateUrl: './header.component.html',
//   styleUrls: ['./header.component.css']
// })
// export class HeaderComponent {
//   @Input() pageTitle = '';
//   @Input() logoSrc = '';
//   @Output() loginEvent = new EventEmitter<void>();
//   @Output() logoutEvent = new EventEmitter<void>();
//
//   searchTerm = '';
//
//   // onSearch() {
//   //   console.log('Căutare pentru:', this.searchTerm);
//   //   // poți apela serviciul aici când îl legi
//   // }
//
//   onSearch() {
//     if (!this.searchTerm.trim()) return;
//     console.log('🔍 Căutare pentru:', this.searchTerm);
//   }
//
// }
//
//
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookService, Book } from '../services/book.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  imports: [FormsModule, CommonModule]
})
export class HeaderComponent {
  @Input() pageTitle = '';
  @Input() logoSrc = '';
  @Output() loginEvent = new EventEmitter<void>();
  @Output() logoutEvent = new EventEmitter<void>();

  searchTerm = '';
  books: Book[] = [];
  errorMessage = '';

  constructor(private bookService: BookService) {}

  onSearch() {
    if (!this.searchTerm.trim()) return;

    this.bookService.searchBooks(this.searchTerm).subscribe({
      next: (results) => {
        this.books = results;
        this.errorMessage = results.length === 0 ? 'Cartea nu a fost găsită.' : '';
      },
      error: () => {
        this.books = [];
        this.errorMessage = 'Eroare la conectarea cu baza de date.';
      }
    });
  }
}
