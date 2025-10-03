import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario, UsuarioPage } from '../../models/usuario.model';
import { UsuarioFormComponent } from './usuario-form/usuario-form.component';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  loading = false;

  displayedColumns: string[] = ['username', 'nomeCompleto', 'email', 'role', 'acoes'];

  constructor(
    private usuarioService: UsuarioService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarUsuarios();
  }

  carregarUsuarios(): void {
    this.loading = true;
    this.usuarioService.listarUsuarios(this.currentPage, this.pageSize).subscribe({
      next: (page: UsuarioPage) => {
        this.usuarios = page.content;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar usuários', 'Fechar', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarUsuarios();
  }

  novoUsuario(): void {
    const dialogRef = this.dialog.open(UsuarioFormComponent, {
      width: '500px',
      data: { usuario: null }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarUsuarios();
      }
    });
  }

  editarUsuario(usuario: Usuario): void {
    const dialogRef = this.dialog.open(UsuarioFormComponent, {
      width: '500px',
      data: { usuario }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarUsuarios();
      }
    });
  }

  excluirUsuario(usuario: Usuario): void {
    if (confirm(`Deseja excluir o usuário ${usuario.username}?`)) {
      this.usuarioService.excluirUsuario(usuario.id!).subscribe({
        next: () => {
          this.snackBar.open('Usuário excluído com sucesso', 'Fechar', { duration: 3000 });
          this.carregarUsuarios();
        },
        error: () => {
          this.snackBar.open('Erro ao excluir usuário', 'Fechar', { duration: 3000 });
        }
      });
    }
  }

  getRoleLabel(role: string): string {
    return role === 'ADMIN' ? 'Administrador' : 'Psicólogo';
  }
}