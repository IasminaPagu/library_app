import { Component, EventEmitter,Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  @Output() onSubmitLoginEvent = new EventEmitter();
  //i create an output form, this way the submit method will be on the content component
  //in the parent component
  //having the login request in the parent component allows me to handle the response and switch
  //from the login form to the auth content component once authentificated
   @Output() onSubmitRegisterEvent = new EventEmitter();

  login: string = "";
  firstName: string = "";
  lastName: string = "";
  password: string = "";
  active: string = "login";
  //now i create the variable which will contain the login and the password


  onLoginTab(): void {
    this.active = "login";
    //this is the method to switch beetween the forms
  }

  onRegisterTab(): void{
    this.active = "register";
  }
  onSubmitLogin(): void{
    this.onSubmitLoginEvent.emit({"login": this.login,"password": this.password})
  }
  //this method will emit the output variable
  onSubmitRegister(): void{
    this.onSubmitRegisterEvent.emit({
      "firstName": this.firstName,
      "lastName": this.lastName,
      "login": this.login,
      "password": this.password})
    //this is the method which will pe used when i submit the register form
  }

}
