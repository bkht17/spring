import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CamundaService } from '../service/camunda.service';
import { CamundaUser } from '../model/camundauser';

@Component({
  selector: 'app-camunda-users-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './camunda-users-list.component.html',
  styleUrls: ['./camunda-users-list.component.scss'],
})
export class CamundaUsersListComponent implements OnInit {
  users: CamundaUser[] = [];
  loading = false;
  error = '';
  searchTerm = '';

  showUserForm = false;
  isEditing = false;

  userForm = {
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  };

  constructor(private camundaService: CamundaService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.camundaService.getCamundaUsers().subscribe({
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

  searchUsers(): void {
    if (this.searchTerm.trim()) {
      this.loading = true;
      this.camundaService.searchCamundaUsers(this.searchTerm).subscribe({
        next: (data) => {
          this.users = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Search failed';
          this.loading = false;
        },
      });
    } else {
      this.loadUsers();
    }
  }

  createUser(): void {
    this.camundaService.createCamundaUser(this.userForm).subscribe({
      next: () => {
        this.loadUsers();
        this.resetForm();
        this.showUserForm = false;
      },
      error: (err) => {
        this.error = 'Failed to create user: ' + (err.error || err.message);
        console.error('Create error:', err);
      },
    });
  }

  updateUser(): void {
    if (this.userForm.id) {
      this.camundaService
        .updateCamundaUser(this.userForm.id, this.userForm)
        .subscribe({
          next: () => {
            this.loadUsers();
            this.resetForm();
            this.showUserForm = false;
          },
          error: (err) => {
            this.error = 'Failed to update user: ' + (err.error || err.message);
            console.error('Update error:', err);
          },
        });
    }
  }

  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.camundaService.deleteCamundaUser(userId).subscribe({
        next: () => {
          this.loadUsers();
        },
        error: (err) => {
          this.error = 'Failed to delete user: ' + (err.error || err.message);
          console.error('Delete error:', err);
        },
      });
    }
  }

  editUser(user: CamundaUser): void {
    this.isEditing = true;
    this.userForm = {
      id: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: '',
    };
    this.showUserForm = true;
  }

  submitForm(): void {
    if (this.isEditing) {
      this.updateUser();
    } else {
      this.createUser();
    }
  }

  resetForm(): void {
    this.userForm = {
      id: '',
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    };
    this.isEditing = false;
  }

  cancelForm(): void {
    this.resetForm();
    this.showUserForm = false;
  }

  addNewUser(): void {
    this.resetForm();
    this.showUserForm = true;
    this.isEditing = false;
  }

  isFormValid(): boolean {
    if (
      !this.userForm.id ||
      !this.userForm.firstName ||
      !this.userForm.lastName ||
      !this.userForm.email
    ) {
      return false;
    }

    if (!this.isEditing && !this.userForm.password) {
      return false;
    }

    return true;
  }
}
