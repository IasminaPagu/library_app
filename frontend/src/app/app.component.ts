// import { Component } from '@angular/core';
// import { RouterOutlet } from '@angular/router';
// import { HeaderComponent } from './header/header.component';
// import {AuthContentComponent} from './auth-content/auth-content.component';
// import {ContentComponent} from './content/content.component';
// @Component({
//   selector: 'app-root',
//   standalone:true,
//   imports: [RouterOutlet, HeaderComponent,AuthContentComponent,ContentComponent],
//   templateUrl: './app.component.html',
//   styleUrl: './app.component.css'
//
// })
// export class AppComponent {
//   title = 'frontend';
// }

import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { AuthContentComponent } from './auth-content/auth-content.component';
import { ContentComponent } from './content/content.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, AuthContentComponent, ContentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';

  @ViewChild(ContentComponent) contentComponent!: ContentComponent;

  onLoginClick() {
    this.contentComponent.showComponent('login');
  }

  onLogoutClick() {
    this.contentComponent.showComponent('welcome');
  }
}


