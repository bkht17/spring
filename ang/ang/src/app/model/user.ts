import { UploadedFile } from './uploadedfile';

export interface User {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  age: number;
  uploadedFile: UploadedFile;
}
