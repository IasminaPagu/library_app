import { Component, OnInit } from '@angular/core';
import { ActivatedRoute }     from '@angular/router';
import { CommonModule }       from '@angular/common';
import { BookService, Book }  from '../../services/book.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';  // ← import!

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book?: Book;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private http: HttpClient        // ← injectăm HttpClient
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.bookService.getBookById(id).subscribe({
          next: data => {
            this.book = data;
            this.errorMessage = '';
          },
          error: () => {
            this.book = undefined;
            this.errorMessage = 'Cartea nu a fost găsită.';
          }
        });
      }
    });
  }

  addToCart(): void {
    if (!this.book) return;

    //const token = localStorage.getItem('jwtToken');
    //console.log('🔑 JWT token from storage:', token);
    const token = window.localStorage.getItem('auth_token');

    // 2) construiești header-ele
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    console.log('📤 Request headers:', { Authorization: headers.get('Authorization') });

    this.http.post<{items:any[]}>(
      'http://localhost:8080/cart/add',
      { bookId: this.book.id, quantity: 1 },
      { headers }
    ).subscribe({ /* ... */ });
  }

addToWishlist(): void {
    if (!this.book) return;

    //const token = localStorage.getItem('jwtToken');
    //console.log('🔑 JWT token from storage:', token);
    const token = window.localStorage.getItem('auth_token');

    // 2) construiești header-ele
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    console.log('📤 Request headers:', { Authorization: headers.get('Authorization') });

    this.http.post<{items:any[]}>(
      'http://localhost:8080/wishlist/add',
      { bookId: this.book.id, quantity: 1 },
      { headers }
    ).subscribe({ /* ... */ });
  }

}
