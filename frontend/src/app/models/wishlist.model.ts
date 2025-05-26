export interface WishlistItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface Wishlist {
  items: WishlistItem[];
}
