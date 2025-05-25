import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CartItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface CartDto {
  items: CartItem[];
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private baseUrl = 'http://localhost:8080/cart';

  constructor(private http: HttpClient) {}

  addToCart(bookId: number) {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(`${this.baseUrl}/add`, { bookId }, { headers });
  }

  getCart(): Observable<CartDto> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<CartDto>(`${this.baseUrl}`, { headers });
  }
}
