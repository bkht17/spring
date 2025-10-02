import { Routes } from '@angular/router';
import { PostComponent } from './post/post.component';
import { UserListComponent } from './users-list/users-list.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { FileListComponent } from './file-list/file-list.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './guard/auth.guard';
import { CamundaUsersListComponent } from './camunda-users-list/camunda-users-list.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] },
  {
    path: 'users/:id',
    component: UserDetailComponent,
    canActivate: [AuthGuard],
  },
  { path: 'post', component: PostComponent, canActivate: [AuthGuard] },
  { path: 'upload', component: FileUploadComponent, canActivate: [AuthGuard] },
  { path: 'files', component: FileListComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/users', pathMatch: 'full' },
  { path: 'camunda-users', component: CamundaUsersListComponent },
];
