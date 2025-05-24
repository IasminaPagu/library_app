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
  constructor() {
    axios.defaults.baseURL = 'http://localhost:8080';
    axios.defaults.headers.post['Content-Type'] = 'application/json';

    // Attach auth token to every outbound request
    axios.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // ensure headers object exists
        config.headers = config.headers || {};

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

  /**
   * Generic request helper
   */
  request(method: string, url: string, data?: any): Promise<any> {
    const cfg: AxiosRequestConfig = { method, url, data };
    return axios(cfg);
  }
}
