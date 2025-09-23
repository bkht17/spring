import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent {
  userForm: FormGroup;
  responseMessage: string = '';

  constructor(private fb: FormBuilder, private userService: ApiService) {
    this.userForm = this.fb.group({
      firstname: '',
      lastname: '',
      email: '',
      password: '',
      age: '',
    });
  }

  onSubmit() {
    if (this.userForm.valid) {
      this.userService.createUser(this.userForm.value).subscribe({});
    }
  }
}
