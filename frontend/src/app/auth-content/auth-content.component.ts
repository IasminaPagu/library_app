import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-auth-content',
  standalone:true,
  imports: [CommonModule],//am importat-o din cauza ngFor, which i use in the auth-content.component.html
  templateUrl: './auth-content.component.html',
  styleUrl: './auth-content.component.css'
})
export class AuthContentComponent {
  data: string[] = [];
//   the variable where i will store the data from the backend

  constructor(private axiosService: AxiosService) { }
// inject the axios service into the controller
  ngOnInit(): void {
    this.axiosService.request(
    "GET",
    "/messages",
    null
    ).then(
    (response) => this.data = response.data
    );
//   request the backend on the initialization of this component
// i call the the request method of the Axios method
// which is a get method to the messages endpoint with no args
// !! poti sa te uiti in backend si sa vezi ca metoda messages nu are nicun args
// and the response i will store in the data variable

  }
}


