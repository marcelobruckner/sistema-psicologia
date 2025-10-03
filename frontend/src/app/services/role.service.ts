import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private authService: AuthService) {}

  isAdmin(): Observable<boolean> {
    return this.authService.currentUser.pipe(
      map(user => user?.role === 'ADMIN')
    );
  }

  isPsicologo(): Observable<boolean> {
    return this.authService.currentUser.pipe(
      map(user => user?.role === 'PSICOLOGO')
    );
  }

  hasRole(role: string): Observable<boolean> {
    return this.authService.currentUser.pipe(
      map(user => user?.role === role)
    );
  }
}