import { WelcomeContentComponent } from './welcome-content/welcome-content.component';
import { Routes } from '@angular/router';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { CartComponent } from './cart/cart.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { LoginPageComponent }      from './login-page/login-page.component';

export const routes: Routes = [
  { path: '', component: WelcomeContentComponent },
  { path: 'login',          component: LoginPageComponent },
  { path: 'books/:id', component: BookDetailsComponent },
  { path: 'cart', component: CartComponent },
  { path: 'wishlist', component: WishlistComponent },
  // alte rute
];
