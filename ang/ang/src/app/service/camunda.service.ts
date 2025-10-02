import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CamundaUser } from '../model/camundauser';
import { CamundaGroup } from '../model/camundagroup';
import { Membership } from '../model/membership';

@Injectable({
  providedIn: 'root',
})
export class CamundaService {
  private apiUrl = 'http://localhost:8080/camunda';

  constructor(private http: HttpClient) {}

  getCamundaUsers(): Observable<CamundaUser[]> {
    return this.http.get<CamundaUser[]>(`${this.apiUrl}/users`);
  }

  getCamundaUser(userId: string): Observable<CamundaUser> {
    return this.http.get<CamundaUser>(`${this.apiUrl}/users/${userId}`);
  }

  createCamundaUser(user: CamundaUser): Observable<string> {
    return this.http.post(`${this.apiUrl}/users`, user, {
      responseType: 'text',
    });
  }

  updateCamundaUser(userId: string, user: CamundaUser): Observable<string> {
    return this.http.put(`${this.apiUrl}/users/${userId}`, user, {
      responseType: 'text',
    });
  }

  deleteCamundaUser(userId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/users/${userId}`, {
      responseType: 'text',
    });
  }

  searchCamundaUsers(query: string): Observable<CamundaUser[]> {
    return this.http.get<CamundaUser[]>(
      `${this.apiUrl}/users/search?q=${query}`
    );
  }

  getCamundaGroups(): Observable<CamundaGroup[]> {
    return this.http.get<CamundaGroup[]>(`${this.apiUrl}/groups`);
  }

  getCamundaGroup(groupId: string): Observable<CamundaGroup> {
    return this.http.get<CamundaGroup>(`${this.apiUrl}/groups/${groupId}`);
  }

  createCamundaGroup(group: CamundaGroup): Observable<string> {
    return this.http.post(`${this.apiUrl}/groups`, group, {
      responseType: 'text',
    });
  }

  updateCamundaGroup(groupId: string, group: CamundaGroup): Observable<string> {
    return this.http.put(`${this.apiUrl}/groups/${groupId}`, group, {
      responseType: 'text',
    });
  }

  deleteCamundaGroup(groupId: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/groups/${groupId}`, {
      responseType: 'text',
    });
  }

  addUserToGroup(membership: Membership): Observable<string> {
    return this.http.post(`${this.apiUrl}/memberships`, membership, {
      responseType: 'text',
    });
  }

  removeUserFromGroup(membership: Membership): Observable<string> {
    return this.http.delete(`${this.apiUrl}/memberships`, {
      body: membership,
      responseType: 'text',
    });
  }

  getUserGroups(userId: string): Observable<CamundaGroup[]> {
    return this.http.get<CamundaGroup[]>(
      `${this.apiUrl}/groups/user/${userId}`
    );
  }

  getGroupUsers(groupId: string): Observable<CamundaUser[]> {
    return this.http.get<CamundaUser[]>(
      `${this.apiUrl}/groups/${groupId}/users`
    );
  }
}
