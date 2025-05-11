import { Routes } from '@angular/router';
import { BookDetailsComponent } from './components/book-details/book-details.component';

export const routes: Routes = [
  { path: 'books/:id', component: BookDetailsComponent },

  // alte rute
];
