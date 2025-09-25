import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../service/api.service';
import { UserFilterComponent } from '../user-filter/user-filter.component';
import { UserFilter } from '../model/userfilter';
import { PageResponse } from '../model/pageresponse';

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

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadUsersWithPagination();
  }

  loadUsersWithPagination(page: number = 0): void {
    this.loading = true;
    this.error = '';
    this.currentPage = page;

    const filter: UserFilter = {
      ...this.currentFilter,
      page: this.currentPage,
      size: this.pageSize,
    };

    this.api.getUsersWithPagination(filter).subscribe({
      next: (response: PageResponse<any>) => {
        this.users = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.pageSize = response.pageSize;
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
    this.currentFilter = filter;
    this.loadUsersWithPagination(0);
  }

  goToDetail(id: number) {
    this.router.navigate(['/users', id]);
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.loadUsersWithPagination(this.currentPage + 1);
    }
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.loadUsersWithPagination(this.currentPage - 1);
    }
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.loadUsersWithPagination(page);
    }
  }

  get pages(): number[] {
    const pages = [];
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages, start + 5);

    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    return pages;
  }

  changePageSize(size: number) {
    this.pageSize = size;
    this.loadUsersWithPagination(0);
  }
}
