// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { Cart } from '../models/cart.model';

// @Injectable({ providedIn: 'root' })
// export class CartService {
//   private apiUrl = 'http://localhost:8080'; // sau din environment

//   constructor(private http: HttpClient) {}

//   addToCart(bookId: number, quantity = 1): Observable<Cart> {
//     return this.http.post<Cart>(`${this.apiUrl}/cart/add`, { bookId, quantity });
//   }

//   getCart(): Observable<Cart> {
//     return this.http.get<Cart>(`${this.apiUrl}/cart`);
//   }
// }
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
  private baseUrl = 'http://localhost:8080/cart'; // ajustat după backend

  constructor(private http: HttpClient) {}

  getCart(): Observable<CartDto> {
    const token = localStorage.getItem('token'); // sau din AuthService
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}` // ❗ FĂRĂ ghilimele extra
    });
  
    console.log("📤 Token trimis:", token); // Adaugă pentru debug
  
    return this.http.get<CartDto>(`${this.baseUrl}`, { headers });
  }
  
  
}
