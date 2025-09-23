import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss'],
})
export class UserDetailComponent implements OnInit {
  userForm!: FormGroup;
  userId!: number;
  message = '';

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    this.userForm = this.fb.group({
      firstname: [''],
      lastname: [''],
      email: [''],
      password: [''],
      age: [''],
    });

    this.api.getUserById(this.userId).subscribe({
      next: (user) => this.userForm.patchValue(user),
      error: () => (this.message = 'failed to load user'),
    });
  }

  updateUser() {
    if (this.userForm.valid) {
      this.api.updateUser(this.userId, this.userForm.value).subscribe({
        next: () => {
          this.message = 'user updated';
          this.router.navigate(['/users']);
        },
        error: () => (this.message = 'update failed'),
      });
    }
  }

  deleteUser() {
    if (confirm('')) {
      this.api.deleteUser(this.userId).subscribe({
        next: () => {
          this.message = 'user deleted';
          this.router.navigate(['/users']);
        },
        error: () => (this.message = 'delete failed'),
      });
    }
  }
}
