import { WelcomeContentComponent } from './welcome-content/welcome-content.component';
import { Routes } from '@angular/router';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { CartComponent } from './cart/cart.component';

export const routes: Routes = [
  { path: '', component: WelcomeContentComponent },
  { path: 'books/:id', component: BookDetailsComponent },
  { path: 'cart', component: CartComponent },

  // alte rute
];
