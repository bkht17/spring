import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../service/api.service';
import { UploadedFile } from '../model/uploadedfile';

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.scss'],
})
export class FileListComponent implements OnInit {
  files: UploadedFile[] = [];
  loading = false;
  error = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadFiles();
  }

  loadFiles(): void {
    this.loading = true;
    this.error = '';

    this.api.getAllFiles().subscribe({
      next: (data) => {
        this.files = data;
        this.loading = false;
      },
      error: (err) => {
        console.log(err);
        this.error = 'Failed to load files';
        this.loading = false;
        console.error('Error loading files:', err);
      },
    });
  }

  downloadFile(fileId: number): void {
    this.api.downloadFile(fileId).subscribe({
      next: (blob) => {
        const file = this.files.find((f) => f.id === fileId);
        if (file) {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = file.filename;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
          window.URL.revokeObjectURL(url);
        }
      },
      error: (err) => {
        alert('Failed to download file');
        console.error('Download error:', err);
      },
    });
  }

  deleteFile(fileId: number): void {
    if (confirm('Are you sure you want to delete this file?')) {
      this.api.deleteFile(fileId).subscribe({
        next: () => {
          alert('File deleted successfully');
          this.loadFiles();
        },
        error: (err) => {
          alert('Failed to delete file');
          console.error('Delete error:', err);
        },
      });
    }
  }
}
