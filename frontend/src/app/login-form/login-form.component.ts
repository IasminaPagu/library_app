import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  @Output() onSubmitLoginEvent = new EventEmitter<{ login: string; password: string }>();
  @Output() onSubmitRegisterEvent = new EventEmitter<{
    firstName: string;
    lastName: string;
    login: string;
    password: string;
  }>();

  // form models & UI state
  active: 'login' | 'register' = 'login';
  login = '';
  password = '';
  firstName = '';
  lastName = '';
  showPassword = false;

  onLoginTab(): void {
    this.active = 'login';
  }

  onRegisterTab(): void {
    this.active = 'register';
  }

  onSubmitLogin(): void {
    this.onSubmitLoginEvent.emit({ login: this.login, password: this.password });
  }

  onSubmitRegister(): void {
    this.onSubmitRegisterEvent.emit({
      firstName: this.firstName,
      lastName: this.lastName,
      login: this.login,
      password: this.password
    });
  }

  toggleShowPassword(): void {
    this.showPassword = !this.showPassword;
  }
}
