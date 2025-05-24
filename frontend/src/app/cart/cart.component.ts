import { Component, OnInit } from '@angular/core';
import { CommonModule }       from '@angular/common';
import { CartService }        from '../services/cart.service';
import { Cart, CartItem }     from '../models/cart.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cart: Cart = { items: [] };
  loading = false;
  error: string | null = null;

  constructor(private cartSvc: CartService) {}

  ngOnInit() {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartSvc.getCart().subscribe({
      next: (c: Cart) => (this.cart = c),
      error: err => (this.error = err.message || 'Eroare la încărcarea coșului'),
      complete: () => (this.loading = false)
    });
  }
}
