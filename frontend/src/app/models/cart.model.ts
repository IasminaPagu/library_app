export interface CartItem {
  bookId: number;
  title: string;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
}
