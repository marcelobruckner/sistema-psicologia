import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { RoleService } from '../../services/role.service';
import { UsuarioService } from '../../services/usuario.service';
import { PacienteService } from '../../services/paciente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser$ = this.authService.currentUser;
  isAdmin$ = this.roleService.isAdmin();
  totalUsuarios = 0;
  totalPacientes = 0;

  constructor(
    private authService: AuthService,
    private router: Router,
    private roleService: RoleService,
    private usuarioService: UsuarioService,
    private pacienteService: PacienteService
  ) {}

  ngOnInit(): void {
    this.carregarEstatisticas();
  }

  carregarEstatisticas(): void {
    // Carregar total de pacientes
    this.pacienteService.listarPacientes(0, 1).subscribe({
      next: (page) => this.totalPacientes = page.totalElements,
      error: () => this.totalPacientes = 0
    });

    // Carregar total de usuários (apenas se for admin)
    this.isAdmin$.subscribe(isAdmin => {
      if (isAdmin) {
        this.usuarioService.listarUsuarios(0, 1).subscribe({
          next: (page) => this.totalUsuarios = page.totalElements,
          error: () => this.totalUsuarios = 0
        });
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}