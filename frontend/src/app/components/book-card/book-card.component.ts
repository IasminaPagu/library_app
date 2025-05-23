import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Book } from '../../services/book.service';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.css']
})
export class BookCardComponent {
  @Input() book!: Book;
}
