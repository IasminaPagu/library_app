// src/app/wishlist/wishlist.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistService, WishlistItem } from '../services/wishlist.service';
import { CartService } from '../services/cart.service';
import { switchMap } from 'rxjs/operators';

interface WishlistItemWithImage extends WishlistItem {
  imageUrl: string;
}

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  wishlistItems: WishlistItemWithImage[] = [];
  errorMessage = '';

  constructor(
    private wishlistService: WishlistService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  private loadWishlist(): void {
    this.wishlistService.getWishlist().subscribe({
      next: dto => {
        // combine any duplicate bookIds
        const agg: Record<number, WishlistItemWithImage> = {};
        dto.items.forEach(it => {
          if (!agg[it.bookId]) {
            agg[it.bookId] = { ...it, imageUrl: `/assets/images/${it.bookId}.jpg` };
          } else {
            agg[it.bookId].quantity += it.quantity;
          }
        });
        this.wishlistItems = Object.values(agg);
      },
      error: () => this.errorMessage = 'Eroare la încărcarea wishlist-ului'
    });
  }

 // src/app/components/wishlist/wishlist.component.ts
 moveToCart(bookId: number): void {
   this.wishlistService.moveToCart(bookId).subscribe({
     next: () => this.loadWishlist(),
     error: () => this.errorMessage = 'Nu am putut muta cartea în coș'
   });
 }

}
