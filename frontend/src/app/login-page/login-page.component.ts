// src/app/login-page/login-page.component.ts
import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AxiosService } from '../axios.service';
import { LoginFormComponent } from '../login-form/login-form.component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ LoginFormComponent ],      // ← bring in your form
  template: `
    <div class="login-page-container">
      <h2>Please Sign In</h2>
      <app-login-form
        (onSubmitLoginEvent)="doLogin($event)"
        (onSubmitRegisterEvent)="doRegister($event)">
      </app-login-form>
    </div>
  `,
  styles: [`
    .login-page-container {
      max-width: 400px;
      margin: 2rem auto;
      padding: 1rem;
      border: 1px solid #ccc;
      border-radius: 8px;
    }
  `]
})
export class LoginPageComponent {
  private returnUrl: string;

  constructor(
    private axios: AxiosService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // Accept `any` so that TS no longer complains
  doLogin(creds: any) {
    this.axios.request('post', '/login', creds)
      .then(resp => {
        this.axios.setAuthToken(resp.data.token);
        this.axios.setAuthLogin(resp.data.login);

        const { login, password } = creds;
        const isAdmin = (login === 'vlad'    && password === 'parolaVlad#')
                     || (login === 'iasmina' && password === 'parolaIasmina#')
                     || (login === 'gety'    && password === 'parolaGety#');
        this.axios.setIsAdmin(isAdmin);

        alert('You have logged in successfully!');
        this.router.navigateByUrl(this.returnUrl);
      })
      .catch(err => {
        console.error('login failed', err);
        alert('Login failed');
      });
  }

  doRegister(signup: any) {
    this.axios.request('post', '/register', signup)
      .then(resp => {
        this.axios.setAuthToken(resp.data.token);
        this.axios.setAuthLogin(resp.data.login);
        this.router.navigateByUrl(this.returnUrl);
      })
      .catch(err => {
        console.error('registration failed', err);
        alert('Registration failed');
      });
  }
}
