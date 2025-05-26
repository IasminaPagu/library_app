import { Component, OnInit } from '@angular/core';

interface Book {
  id: number;
  title: string;
  author: string;
  category: string;
  year: number;
  imageUrl: string;
  description: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  books: Book[] = [
    { id: 1, title: 'Moby Dick', author: 'Herman Melville', category: 'Adventure', year: 1851, imageUrl: 'assets/books/moby-dick.jpg', description: 'Epopeea obsesiei căpitanului Ahab față de balena albă.' },
    { id: 2, title: 'Pride and Prejudice', author: 'Jane Austen', category: 'Romance', year: 1813, imageUrl: 'assets/books/pride-prejudice.jpg', description: 'O poveste clasică despre dragoste și prejudecăți sociale.' },
    { id: 3, title: 'The Hobbit', author: 'J.R.R. Tolkien', category: 'Fantasy', year: 1937, imageUrl: 'assets/books/the-hobbit.jpg', description: 'Aventurile lui Bilbo Baggins într-o lume fantastică.' },
    { id: 4, title: '1984', author: 'George Orwell', category: 'Sci-Fi', year: 1949, imageUrl: 'assets/books/1984.jpg', description: 'O distopie clasică despre supraveghere și libertate.' },
    { id: 5, title: 'Sherlock Holmes', author: 'Arthur Conan Doyle', category: 'Mystery', year: 1892, imageUrl: 'assets/books/sherlock.jpg', description: 'Aventurile celebrului detectiv britanic.' },
    { id: 6, title: 'The Little Prince', author: 'Antoine de Saint-Exupéry', category: 'Children', year: 1943, imageUrl: 'assets/books/little-prince.jpg', description: 'O poveste poetică despre copilărie și sensul vieții.' },
    { id: 7, title: 'Sapiens', author: 'Yuval Noah Harari', category: 'History', year: 2011, imageUrl: 'assets/books/sapiens.jpg', description: 'O scurtă istorie a omenirii.' },
    { id: 8, title: 'Dune', author: 'Frank Herbert', category: 'Sci-Fi', year: 1965, imageUrl: 'assets/books/dune.jpg', description: 'Saga planetelor deșertice și a intrigilor politice.' },
    { id: 9, title: 'Harry Potter', author: 'J.K. Rowling', category: 'Fantasy', year: 1997, imageUrl: 'assets/books/harry-potter.jpg', description: 'Aventurile unui tânăr vrăjitor la Hogwarts.' },
    { id: 10, title: 'To Kill a Mockingbird', author: 'Harper Lee', category: 'Classic', year: 1960, imageUrl: 'assets/books/mockingbird.jpg', description: 'O poveste despre justiție și prejudecăți în America de Sud.' }
  ];
  categories: string[] = [];
  selectedCategory: string = 'All';
  sortKey: string = 'title';
  sortDirection: 'asc' | 'desc' = 'asc';

  ngOnInit() {
    this.categories = ['All', ...Array.from(new Set(this.books.map(b => b.category)))];
  }

  get filteredBooks(): Book[] {
    let filtered = this.selectedCategory === 'All'
      ? this.books
      : this.books.filter(b => b.category === this.selectedCategory);
    return filtered.sort((a, b) => {
      const valA = (a as any)[this.sortKey];
      const valB = (b as any)[this.sortKey];
      if (valA < valB) return this.sortDirection === 'asc' ? -1 : 1;
      if (valA > valB) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
  }

  setCategory(category: string) {
    this.selectedCategory = category;
  }

  setSort(key: string) {
    if (this.sortKey === key) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKey = key;
      this.sortDirection = 'asc';
    }
  }
} 