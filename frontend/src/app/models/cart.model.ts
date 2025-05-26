export interface CartItem {
  bookId: number;
  title: string;
  quantity: number;
  imageUrl?: string;
}

export interface Cart {
  items: CartItem[];
}
