import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WishlistItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface WishlistDto {
  items: WishlistItem[];
}

@Injectable({
  providedIn: 'root',
})
export class WishlistService {
  private baseUrl = 'http://localhost:8080/wishlist';

  constructor(private http: HttpClient) {}

  addToWishlist(bookId: number) {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.post(`${this.baseUrl}/add`, { bookId }, { headers });
  }

  getWishlist(): Observable<WishlistDto> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<WishlistDto>(`${this.baseUrl}`, { headers });
  }
}
