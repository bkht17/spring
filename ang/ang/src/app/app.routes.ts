import { Routes } from '@angular/router';
import { PostComponent } from './post/post.component';
import { UserListComponent } from './users-list/users-list.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { FileUploadComponent } from './file-upload/file-upload.component';

export const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'users/:id', component: UserDetailComponent },
  { path: 'post', component: PostComponent },
  { path: 'upload', component: FileUploadComponent },
];
