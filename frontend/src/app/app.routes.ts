import { Routes } from '@angular/router';

import { BookDetailsComponent } from './components/book-details/book-details.component';

export const routes: Routes = [
  { path: 'books/:id', component: BookDetailsComponent },

  // alte rute

import { LoginComponent } from './components/login.component';
import { HomeComponent } from './components/home.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent }
];
