import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface CartItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface CartDto {
  items: CartItem[];
}
@Injectable({ providedIn: 'root' })
export class CartService {
  private baseUrl = `${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) {}

  addToCart(bookId: number, quantity: number = 1): Observable<void> {
    const headers = this.authHeaders();
    return this.http.post<void>(
      `${environment.apiUrl}/wishlist/move-to-cart`,
      { bookId, quantity },
      { headers }
    );
  }

  getCart(): Observable<CartDto> {
    const headers = this.authHeaders();
    return this.http.get<CartDto>(this.baseUrl, { headers });
  }

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }
  finalizeOrder(): Observable<any> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post('http://localhost:8080/cart/checkout', {}, { headers });
  }
}
