import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart } from '../models/cart.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  private apiUrl = 'http://localhost:8080'; // sau din environment

  constructor(private http: HttpClient) {}

  addToCart(bookId: number, quantity = 1): Observable<Cart> {
    return this.http.post<Cart>(`${this.apiUrl}/cart/add`, { bookId, quantity });
  }

  getCart(): Observable<Cart> {
    return this.http.get<Cart>(`${this.apiUrl}/cart`);
  }
}
