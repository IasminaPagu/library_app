// src/app/login-form/login-form.component.ts

import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule }            from '@angular/common';
import { FormsModule }             from '@angular/forms';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  @Output() onSubmitLoginEvent    = new EventEmitter<{login:string, password:string}>();
  @Output() onSubmitRegisterEvent = new EventEmitter<{firstName:string, lastName:string, login:string, password:string}>();

  // which tab is active
  active: 'login' | 'register' = 'login';

  // form models
  login     = '';
  password  = '';
  firstName = '';
  lastName  = '';

  // show/hide password
  showPassword = false;

  // real-time strength flags
  hasMinLength   = false;
  hasUpperCase   = false;
  hasSpecialChar = false;


  // --- tab switching ---
  onLoginTab()    { this.active = 'login'; }
  onRegisterTab() { this.active = 'register'; }

  // --- form submissions ---
  onSubmitLogin() {
    this.onSubmitLoginEvent.emit({ login: this.login, password: this.password });
  }

  onSubmitRegister() {
    this.onSubmitRegisterEvent.emit({
      firstName: this.firstName,
      lastName:  this.lastName,
      login:     this.login,
      password:  this.password
    });
  }

  // --- password visibility toggle ---
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  // --- run on every keystroke in the password field ---
  onPasswordInput(value: string) {
    this.password        = value;
    this.hasMinLength    = value.length >= 8;
    this.hasUpperCase    = /[A-Z]/.test(value);
    this.hasSpecialChar  = /[!@#\$%\^&\*]/.test(value);
  }

  // disable the Register button until all criteria are met
  get registerDisabled() {
    return !(this.hasMinLength && this.hasUpperCase && this.hasSpecialChar);
  }
}
