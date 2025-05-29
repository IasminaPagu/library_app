import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../services/cart.service';
import { CartItem } from '../models/cart.model';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: (CartItem & { imageUrl: string })[] = [];

  constructor(private cartService: CartService, private http: HttpClient) { }


  ngOnInit(): void {
    this.cartService.getCart().subscribe(cart => {
      const aggregated: { [key: number]: CartItem & { imageUrl: string } } = {};

      for (const item of cart.items) {
        if (!aggregated[item.bookId]) {
          aggregated[item.bookId] = {
            ...item,
            imageUrl: `http://localhost:8080/uploads/${item.bookId}.jpg`
          };
        } else {
          aggregated[item.bookId].quantity += item.quantity;
        }
      }

      this.cartItems = Object.values(aggregated);
    }
  );
  }
  // finalizeOrder(): void {
  //   this.cartService.finalizeOrder().subscribe({
  //     next: (res) => {
  //       alert("Comanda a fost finalizată cu succes!");
  //       this.cartItems = []; // golește coșul din UI
  //     },
  //     error: (err) => {
  //       alert(err.error?.message || "Eroare la finalizarea comenzii.");
  //     }
  //   });
  // }
  finalizeOrder(): void {
    const token = window.localStorage.getItem('auth_token');
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    const updatedCart = this.cartItems.map(item => ({
      bookId: item.bookId,
      quantity: item.quantity
    }));

    this.http.post(
      'http://localhost:8080/cart/checkout',
      { items: updatedCart },  // ← aici trimitem cantitățile reale
      { headers }
    ).subscribe({
      next: () => {
        alert("Comanda a fost finalizată cu succes!");
        this.cartItems = [];
      },
      error: (err) => {
        alert(err.error?.message || "Eroare la finalizarea comenzii.");
      }
    });
  }
  

  
}
