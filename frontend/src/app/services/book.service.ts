// searchBooks(title: string): Observable<Book[]> {
//   const token = localStorage.getItem('token');
//   const headers = new HttpHeaders({
//     Authorization: `Bearer ${token}`
//   });

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// export interface Book {
//   id: number;
//   title: string;
//   author: string;
//   isbn: string;
//   publishedYear: number;
// }

export interface Book {
  description: string;
  id: number;
  title: string;
  author: string;
  isbn: string;
  publishedYear: number;
  category: string;
  imageUrl?: string; // imagini fictive
}


@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8080/books/search';

  constructor(private http: HttpClient) {}

  searchBooks(title: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}?title=${title}`);
  }
  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`http://localhost:8080/books/${id}`);
  }

}
