// src/app/axios.service.ts
import { Injectable } from '@angular/core';
import axios, {
  AxiosRequestConfig,
  InternalAxiosRequestConfig
} from 'axios';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {
  private tokenKey = 'auth_token';
  private loginKey = 'auth_login';
  private adminKey = 'is_admin';

  constructor() {
    axios.defaults.baseURL = 'http://localhost:8080';
    axios.defaults.headers.post['Content-Type'] = 'application/json';

    // Attach auth token to every outbound request
    axios.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // ensure headers object exists
        config.headers = config.headers || {};

        if (!(config.data instanceof FormData)) {
                 config.headers['Content-Type'] = 'application/json';
                } else {
                 delete config.headers['Content-Type'];
                }

        const token = this.getAuthToken();
        if (token) {
          // mutate the existing headers
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      error => Promise.reject(error)
    );
  }

  getAuthToken(): string | null {
    return window.localStorage.getItem('auth_token');
  }

  setAuthToken(token: string | null): void {
    if (token) {
      window.localStorage.setItem('auth_token', token);
    } else {
      window.localStorage.removeItem('auth_token');
    }
  }

  isAdmin(): boolean {
    const l = this.getAuthLogin();
    return l === 'vlad' || l === 'iasmina' || l === 'gety';
  }
setIsAdmin(flag: boolean): void {
    window.localStorage.setItem(this.adminKey, flag ? 'true' : 'false');
  }

  /** Returnează true dacă user-ul s-a logat cu credențiale de admin */
  getIsAdmin(): boolean {
    return window.localStorage.getItem(this.adminKey) === 'true';
  }

  request(method: string, url: string, data?: any): Promise<any> {
    const cfg: AxiosRequestConfig = { method, url, data };
    return axios(cfg);
  }

getAuthLogin(): string | null {
    return window.localStorage.getItem(this.loginKey);
  }

  setAuthLogin(login: string | null): void {
    if (login) {
      window.localStorage.setItem(this.loginKey, login);
    } else {
      window.localStorage.removeItem(this.loginKey);
    }
  }

logout(): void {
    // ştergi JWT-ul
    window.localStorage.removeItem(this.tokenKey);
    // ştergi login-ul
    window.localStorage.removeItem(this.loginKey);
  }
}
