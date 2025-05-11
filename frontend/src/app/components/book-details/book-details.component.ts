import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookService, Book } from '../../services/book.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book?: Book;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService
  ) {}
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) {
        this.bookService.getBookById(id).subscribe({
          next: (data) => {
            this.book = data;
            this.errorMessage = '';
          },
          error: () => {
            this.book = undefined;
            this.errorMessage = 'Cartea nu a fost găsită.';
          }
        });
      }
    });
  }

}
