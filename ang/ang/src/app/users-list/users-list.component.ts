import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../service/api.service';
import { UserFilterComponent } from '../user-filter/user-filter.component';
import { UserFilter } from '../model/userfilter';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, UserFilterComponent],
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss'],
})
export class UserListComponent implements OnInit {
  users: any[] = [];
  loading = true;
  error = '';
  currentFilter: UserFilter = {};

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(filter?: UserFilter): void {
    this.loading = true;
    this.error = '';
    this.currentFilter = filter || {};

    this.api.getUsersWithFilter(this.currentFilter).subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load users';
        this.loading = false;
        console.error('Error loading users:', err);
      },
    });
  }

  onFilterChange(filter: UserFilter) {
    this.loadUsers(filter);
  }

  goToDetail(id: number) {
    this.router.navigate(['/users', id]);
  }
}
