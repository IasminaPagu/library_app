// welcome-content.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';      // for *ngFor
import { RouterModule } from '@angular/router';      // for [routerLink]
import { AxiosService } from '../axios.service';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

interface Book {
  id?: number;
   title: string;
   author: string;
   isbn: string;
   publishedYear: number;
   category: string;
   description: string;
   imageUrl: string;
   stock:         number;
}

type SortMode = 'default' | 'asc' | 'desc';

@Component({
  selector: 'app-welcome-content',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './welcome-content.component.html',
  styleUrls: ['./welcome-content.component.css']
})
export class WelcomeContentComponent implements OnInit {

  books: Book[] = [];
  private originalBooks: Book[] = [];
  sortMode: SortMode = 'default';

  formVisible = false;
  formMode: 'create'|'update' = 'create';
  currentBook?: Book;
  bookForm: FormGroup;

  selectedFile: File | null = null;
  previewUrl:   string | null = null;


  showFilters = false;
    filterCategory = '';
    filterAuthor = '';
    filterInStockOnly: boolean = false;
    filterYearMin: number | null = null;
    filterYearMax: number | null = null;

    uniqueAuthors: string[] = [];
    uniqueCategories: string[] = [];

 constructor(
     public axiosService: AxiosService,
     private fb: FormBuilder
   ) {
     this.bookForm = this.fb.group({
       title:         [''],
       author:        [''],
       isbn:          [''],
       publishedYear: [new Date().getFullYear()],
       category:      [''],
       description:   [''],
       imageUrl:      [''],
       stock:         [0]
     });
   }

  ngOnInit(): void {
    this.loadBooks();
  }

  private loadBooks(): void {
      this.axiosService
        .request('GET', '/books')
        .then(response => {
          this.originalBooks = response.data;
          this.uniqueAuthors = [...new Set(this.originalBooks.map(b => b.author))];
          this.uniqueCategories = [...new Set(this.originalBooks.map(b => b.category))];
          this.applySort();
          this.applyFilters();
        })
        .catch(err => console.error('could not load books', err));
    }

  toggleFilters() {
      this.showFilters = !this.showFilters;
    }

    applyFilters() {
      this.books = this.originalBooks.filter(book => {
        const categoryMatch = this.filterCategory
          ? book.category.toLowerCase().includes(this.filterCategory.toLowerCase())
          : true;
        const authorMatch = this.filterAuthor
          ? book.author.toLowerCase().includes(this.filterAuthor.toLowerCase())
          : true;
        const stockMatch = this.filterInStockOnly ? book.stock > 0 : true;
        const yearMatch = (
          (!this.filterYearMin || book.publishedYear >= this.filterYearMin) &&
          (!this.filterYearMax || book.publishedYear <= this.filterYearMax)
        );
        return categoryMatch && authorMatch && stockMatch && yearMatch;
      });
    }

  exportBooksToCSV() {
      if (!this.axiosService.isAdmin()) {
        alert('Doar adminii pot exporta datele!');
        return;
      }

      this.axiosService.request('GET', '/books').then((response: any) => {
        const books = response.data;

        if (!books || books.length === 0) {
          alert('Nu există cărți de exportat.');
          return;
        }

        const separator = ';';
        const header = ['ID', 'Titlu', 'Autor', 'ISBN', 'An Publicare', 'Categorie', 'Descriere'];
        const rows = books.map((book: any) => [
          book.id,
          book.title?.replace(/"/g, '""'),
          book.author?.replace(/"/g, '""'),
          book.isbn,
          book.publishedYear,
          book.category,
          book.description?.replace(/"/g, '""') || ''
        ]);

        let csvContent = header.join(separator) + '\n';
        rows.forEach((row: any[]) => {
          csvContent += row.map((value: any) => `"${value}"`).join(separator) + '\n';
        });

        const BOM = '\uFEFF';
        const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' });
        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.setAttribute('download', 'carti_export.csv');
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      }).catch(err => {
        console.error('Eroare la export:', err);
        alert('A apărut o eroare la exportul cărților.');
      });
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

openCreateForm() {
    this.formMode = 'create';
    this.currentBook = undefined;
    this.bookForm.reset({
    publishedYear: new Date().getFullYear()
    });
    this.formVisible = true;
    this.selectedFile = null;
    this.previewUrl   = null;
  }

openEditForm(book: Book) {
    this.formMode = 'update';
    this.currentBook = book;
    this.bookForm.patchValue(book);
    this.formVisible = true;
    this.selectedFile = null;
    this.previewUrl   = null
  }

  cancelForm() {
    this.formVisible = false;
  }

  submitForm() {
    const dto: Book = this.bookForm.value;

    if (this.formMode === 'create') {
      // 1) salvează metadate
      this.axiosService
        .request('POST','/books', dto)
        .then(resp => {
          const newBook: Book = resp.data;
          if (this.selectedFile) {
            // 2) apoi upload imagine sub /books/{id}/image
            const form = new FormData();
            form.append('file', this.selectedFile);
            return this.axiosService.request(
              'POST',
              `/books/${newBook.id}/image`,
              form
            );
          }
          // **returnez un Promise gol** pentru ca lanţul .then() următor să primească întotdeauna ceva
          return Promise.resolve();
        })
        .then(() => {
          this.formVisible = false;
          this.loadBooks();
        })
        .catch(err => console.error('add failed', err));

    } else if (this.formMode === 'update' && this.currentBook?.id != null) {
      // 1) update metadate
      this.axiosService
        .request('PUT', `/books/${this.currentBook.id}`, dto)
        .then(() => {
          if (this.selectedFile) {
            const form = new FormData();
            form.append('file', this.selectedFile!);
            return this.axiosService.request(
              'POST',
              `/books/${this.currentBook!.id}/image`,
              form
            );
          }
          return Promise.resolve();
        })
        .then(() => {
          this.formVisible = false;
          this.loadBooks();
        })
        .catch(err => console.error('update failed', err));
    }
  }


// addBook() {
//     const title = prompt('Titlul noii cărţi:');
//     if (!title) return;
//
//     // construim un payload minim; la runtime originalBooks include şi restul câmpurilor
//     const payload: any = { title };
//
//     this.axiosService.request('POST','/books', payload)
//       .then(() => this.loadBooks())
//       .catch(err => console.error('add failed', err));
//   }
//
//   editBook(book: Book) {
//     const newTitle = prompt('Modifică titlu:', book.title);
//     if (newTitle == null) return;
//
//     // găsim obiectul complet în originalBooks
//     const orig = this.originalBooks.find(b => b.id === book.id);
//     if (!orig) return;
//
//     const updated = { ...orig, title: newTitle };
//     this.axiosService.request('PUT', `/books/${book.id}`, updated)
//       .then(() => this.loadBooks())
//       .catch(err => console.error('update failed', err));
//   }

  deleteBook(book: Book) {
    if (!confirm(`Ştergi cartea “${book.title}”?`)) return;

    this.axiosService.request('DELETE', `/books/${book.id}`)
      .then(() => this.loadBooks())
      .catch(err => console.error('delete failed', err));
  }

  onFileSelected(event: Event) {
      const inp = event.target as HTMLInputElement;
      if (!inp.files || inp.files.length===0) return;
      this.selectedFile = inp.files[0];
      // preview client-side
      const reader = new FileReader();
      reader.onload = () => this.previewUrl = reader.result as string;
      reader.readAsDataURL(this.selectedFile);
    }
}
