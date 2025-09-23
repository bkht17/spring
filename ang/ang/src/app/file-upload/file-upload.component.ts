import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  isDragging = false;

  constructor(private api: ApiService) {}

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;

    if (event.dataTransfer?.files.length) {
      this.selectedFile = event.dataTransfer.files[0];
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
    }
  }

  uploadFile() {
    if (!this.selectedFile) return;

    this.api.uploadFile(this.selectedFile).subscribe({
      next: (res) => {
        alert('Success');
        this.selectedFile = null;
      },
      error: (err) => {
        alert('File upload failed');
        console.error('Upload error:', err);
      },
    });
  }
}
