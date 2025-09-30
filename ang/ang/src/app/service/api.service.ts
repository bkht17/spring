import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpParams,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UploadedFile } from '../model/uploadedfile';
import { UserFilter } from '../model/userfilter';
import { PageResponse } from '../model/pageresponse';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  private handleError = (error: HttpErrorResponse) => {
    if (error.status === 401 || error.status === 403) {
      // Token expired or invalid
      this.authService.logout();
      this.router.navigate(['/login']);
    }
    return throwError(() => error);
  };

  getUsers() {
    return this.http
      .get<any[]>(`${this.apiUrl}/users`)
      .pipe(catchError(this.handleError));
  }

  getUserById(id: number) {
    return this.http
      .get<any>(`${this.apiUrl}/users/${id}`)
      .pipe(catchError(this.handleError));
  }

  createUser(user: User): Observable<any> {
    return this.http
      .post(`${this.apiUrl}/users`, user, {
        responseType: 'text',
      })
      .pipe(catchError(this.handleError));
  }

  updateUser(id: number, user: User) {
    return this.http
      .put(`${this.apiUrl}/users/${id}`, user, {
        responseType: 'text',
      })
      .pipe(catchError(this.handleError));
  }

  deleteUser(id: number) {
    return this.http
      .delete(`${this.apiUrl}/users/${id}`, {
        responseType: 'text',
      })
      .pipe(catchError(this.handleError));
  }

  getAllFiles(): Observable<UploadedFile[]> {
    return this.http
      .get<UploadedFile[]>(`${this.apiUrl}/files`)
      .pipe(catchError(this.handleError));
  }

  downloadFile(id: number): Observable<Blob> {
    return this.http
      .get(`${this.apiUrl}/files/${id}`, {
        responseType: 'blob',
      })
      .pipe(catchError(this.handleError));
  }

  deleteFile(id: number): Observable<any> {
    return this.http
      .delete(`${this.apiUrl}/files/${id}`)
      .pipe(catchError(this.handleError));
  }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http
      .post(`${this.apiUrl}/files/upload`, formData, {
        responseType: 'text',
      })
      .pipe(catchError(this.handleError));
  }

  getUsersWithFilter(filter: UserFilter): Observable<any[]> {
    let params = new HttpParams();

    if (filter.firstname) {
      params = params.set('firstname', filter.firstname);
    }
    if (filter.lastname) {
      params = params.set('lastname', filter.lastname);
    }
    if (filter.age) {
      params = params.set('age', filter.age.toString());
    }

    return this.http
      .get<any[]>(`${this.apiUrl}/users`, { params })
      .pipe(catchError(this.handleError));
  }

  getUsersWithPagination(filter: UserFilter): Observable<PageResponse<any>> {
    let params = new HttpParams();

    if (filter.firstname) {
      params = params.set('firstname', filter.firstname);
    }
    if (filter.lastname) {
      params = params.set('lastname', filter.lastname);
    }
    if (filter.age) {
      params = params.set('age', filter.age.toString());
    }
    if (filter.page !== undefined) {
      params = params.set('page', filter.page.toString());
    }
    if (filter.size !== undefined) {
      params = params.set('size', filter.size.toString());
    }

    return this.http
      .get<PageResponse<any>>(`${this.apiUrl}/users/page`, {
        params,
      })
      .pipe(catchError(this.handleError));
  }
}
