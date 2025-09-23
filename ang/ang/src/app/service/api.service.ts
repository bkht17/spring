import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { UploadedFile } from '../model/uploadedfile';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getUsers() {
    return this.http.get<any[]>(`${this.apiUrl}/users`);
  }

  getUserById(id: number) {
    return this.http.get<any>(`${this.apiUrl}/users/${id}`);
  }

  createUser(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/users`, user, {
      responseType: 'text',
    });
  }

  updateUser(id: number, user: any) {
    return this.http.put(`${this.apiUrl}/users/${id}`, user, {
      responseType: 'text',
    });
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.apiUrl}/users/${id}`, {
      responseType: 'text',
    });
  }

  getAllFiles(): Observable<UploadedFile[]> {
    return this.http.get<UploadedFile[]>(`${this.apiUrl}/files`);
  }

  downloadFile(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/files/${id}`, {
      responseType: 'blob',
    });
  }

  deleteFile(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/files/${id}`);
  }

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.apiUrl}/files/upload`, formData, {
      responseType: 'text',
    });
  }
}
