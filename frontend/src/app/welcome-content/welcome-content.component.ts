import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';      // for *ngFor
import { RouterModule } from '@angular/router';      // for [routerLink]
import { AxiosService } from '../axios.service';

interface Book {
  id: number;
  title: string;
  imageUrl?: string;  // adjust if your API returns a different field
}

@Component({
  selector: 'app-welcome-content',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './welcome-content.component.html',
  styleUrls: ['./welcome-content.component.css']
})
export class WelcomeContentComponent implements OnInit {
  books: Book[] = [];

  constructor(private axiosService: AxiosService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  private loadBooks(): void {
    this.axiosService
      .request('GET', '/books')      // ensure your backend exposes GET /books returning all books
      .then(response => {
        this.books = response.data;
      })
      .catch(err => {
        console.error('could not load books', err);
      });
  }
}
