import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
}

export interface AuthRequest {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private tokenKey = 'jwt_token';
  private usernameKey = 'username';
  private roleKey = 'role';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(
    this.hasToken()
  );
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  login(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/login`, authRequest)
      .pipe(
        tap((response) => {
          this.setToken(response.token);
          this.setUsername(response.username);
          this.setRole(response.role);
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  register(authRequest: AuthRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, authRequest, {
      responseType: 'text',
    });
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usernameKey);
    localStorage.removeItem(this.roleKey);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.usernameKey);
  }

  getRole(): string | null {
    return localStorage.getItem(this.roleKey);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  private setUsername(username: string): void {
    localStorage.setItem(this.usernameKey, username);
  }

  private setRole(role: string): void {
    localStorage.setItem(this.roleKey, role);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  isLoggedIn(): boolean {
    return this.hasToken();
  }
}
