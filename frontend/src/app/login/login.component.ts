import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';      // pentru *ngIf, etc.
import { FormsModule } from '@angular/forms';        // pentru ngModel și ngForm
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {
  username = '';
  password = '';
  loginError = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const loginPayload = {
      username: this.username,
      password: this.password
    };

    this.http.post<any>('http://localhost:8080/api/auth/login', loginPayload)
      .subscribe({
        next: response => {
          localStorage.setItem('token', response.token);
          this.router.navigate(['/home']); // sau orice altă pagină de post-login
        },
        error: err => {
          this.loginError = 'Invalid username or password';
          console.error(err);
        }
      });
  }
}
