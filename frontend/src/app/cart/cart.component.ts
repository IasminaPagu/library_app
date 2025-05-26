import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../services/cart.service';
import { CartItem } from '../models/cart.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: (CartItem & { imageUrl: string })[] = [];

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.cartService.getCart().subscribe(cart => {
      const aggregated: { [key: number]: CartItem & { imageUrl: string } } = {};

      for (const item of cart.items) {
        if (!aggregated[item.bookId]) {
          aggregated[item.bookId] = {
            ...item,
            imageUrl: '/assets/images/' + item.bookId + '.jpg'
          };
        } else {
          aggregated[item.bookId].quantity += item.quantity;
        }
      }

      this.cartItems = Object.values(aggregated);
    });
  }
}
