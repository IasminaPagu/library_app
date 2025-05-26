import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistService } from '../services/wishlist.service';
import { WishlistItem } from '../models/wishlist.model';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  wishlistItems: (WishlistItem & { imageUrl: string })[] = [];

  constructor(private wishlistService: WishlistService) { }

  ngOnInit(): void {
    this.wishlistService.getWishlist().subscribe(wishlist => {
      const aggregated: { [key: number]: WishlistItem & { imageUrl: string } } = {};

      for (const item of wishlist.items) {
        if (!aggregated[item.bookId]) {
          aggregated[item.bookId] = {
            ...item,
            imageUrl: '/assets/images/' + item.bookId + '.jpg'
          };
        } else {
          aggregated[item.bookId].quantity += item.quantity;
        }
      }

      this.wishlistItems = Object.values(aggregated);
    });
  }
}
