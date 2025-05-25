import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginFormComponent} from '../login-form/login-form.component';
import { AxiosService } from '../axios.service';
import { ButtonsComponent } from '../buttons/buttons.component';
import { WelcomeContentComponent } from '../welcome-content/welcome-content.component';
import { AuthContentComponent } from '../auth-content/auth-content.component';


@Component({
  selector: 'app-content',
  standalone:true,
  imports: [LoginFormComponent,
            CommonModule,
            ButtonsComponent,
            // WelcomeContentComponent,
            AuthContentComponent],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css'
})
export class ContentComponent {

  componentToShow: string = "welcome";
  constructor(private axiosService: AxiosService) {}

  showComponent(componentToShow: string):void{
    this.componentToShow = componentToShow;
  }

  onLogin(input: any): void {
    this.axiosService.request(
    "POST",
    "/login",
    {
      login: input.login,
       password: input.password
    }
    //and in the response of the login and register endpoints
    //a save back the token
    ).then(response => {
      this.axiosService.setAuthToken(response.data.token);
       this.componentToShow = "messages";
      });
  }


  onRegister(input: any): void {
    this.axiosService.request(
    "POST",
    "/register",
    {
      firstName: input.firstName,
      lastName: input.lastName,
      login: input.login,
      password: input.password
    }
    ).then(response => {
           this.axiosService.setAuthToken(response.data.token);
            this.componentToShow = "messages";
           });
  }

}
