import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserFilter } from '../model/userfilter';

@Component({
  selector: 'app-user-filter',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-filter.component.html',
  styleUrls: ['./user-filter.component.scss'],
})
export class UserFilterComponent {
  @Output() filterChange = new EventEmitter<UserFilter>();

  filterForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      firstname: [''],
      lastname: [''],
      age: [''],
    });
  }

  applyFilter() {
    const filter: UserFilter = {
      firstname: this.filterForm.value.firstname || undefined,
      lastname: this.filterForm.value.lastname || undefined,
      age: this.filterForm.value.age
        ? Number(this.filterForm.value.age)
        : undefined,
    };

    this.filterChange.emit(filter);
  }

  clearFilter() {
    this.filterForm.reset();
    this.filterChange.emit({});
  }
}
