// welcome-content.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';      // for *ngFor
import { RouterModule } from '@angular/router';      // for [routerLink]
import { AxiosService } from '../axios.service';
import { FormsModule } from '@angular/forms';

interface Book {
  id: number;
  title: string;
  imageUrl?: string;
}

type SortMode = 'default' | 'asc' | 'desc';

@Component({
  selector: 'app-welcome-content',
  standalone: true,
  imports: [
    CommonModule,
        FormsModule,
    RouterModule
  ],
  templateUrl: './welcome-content.component.html',
  styleUrls: ['./welcome-content.component.css']
})
export class WelcomeContentComponent implements OnInit {
  books: Book[] = [];
  private originalBooks: Book[] = [];
  sortMode: SortMode = 'default';

  constructor(private axiosService: AxiosService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  private loadBooks(): void {
    this.axiosService
      .request('GET', '/books')
      .then(response => {
        this.originalBooks = response.data;
        this.applySort();
      })
      .catch(err => console.error('could not load books', err));
  }

  /** Called whenever sortMode changes or on initial load */
  applySort() {
    if (this.sortMode === 'default') {
      this.books = [...this.originalBooks];
    } else if (this.sortMode === 'asc') {
      this.books = [...this.originalBooks].sort((a, b) =>
        a.title.localeCompare(b.title, undefined, { sensitivity: 'base' })
      );
    } else {
      this.books = [...this.originalBooks].sort((a, b) =>
        b.title.localeCompare(a.title, undefined, { sensitivity: 'base' })
      );
    }
  }
}
