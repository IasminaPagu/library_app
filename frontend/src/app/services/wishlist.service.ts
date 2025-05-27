import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface WishlistItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface WishlistDto {
  items: WishlistItem[];
}

@Injectable({ providedIn: 'root' })
export class WishlistService {
  private baseUrl = `${environment.apiUrl}/wishlist`; // ← use env.API

  constructor(private http: HttpClient) {}

  addToWishlist(bookId: number) {
    const headers = this.authHeaders();
    return this.http.post(`${this.baseUrl}/add`, { bookId }, { headers });
  }

  getWishlist(): Observable<WishlistDto> {
    const headers = this.authHeaders();
    return this.http.get<WishlistDto>(this.baseUrl, { headers });
  }


removeFromWishlist(bookId: number): Observable<any> {
    const token = localStorage.getItem('auth_token')!;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params  = new HttpParams().set('bookId', bookId.toString());

    return this.http.delete(
      `${this.baseUrl}/remove`,
      { headers, params }
    );
  }

moveToCart(bookId: number): Observable<void> {
    const token = localStorage.getItem('auth_token') || '';
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.post<void>(
      `${this.baseUrl}/move-to-cart`,
      { bookId },
      { headers }
    );
  }

  /** helper to grab your JWT from storage and set the header */
  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

}
