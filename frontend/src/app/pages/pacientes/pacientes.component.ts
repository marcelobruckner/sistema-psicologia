import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PacienteService } from '../../services/paciente.service';
import { Paciente, PacientePage } from '../../models/paciente.model';
import { PacienteFormComponent } from './paciente-form/paciente-form.component';

@Component({
  selector: 'app-pacientes',
  templateUrl: './pacientes.component.html',
  styleUrls: ['./pacientes.component.scss']
})
export class PacientesComponent implements OnInit {
  pacientes: Paciente[] = [];
  totalElements = 0;
  pageSize = 10;
  currentPage = 0;
  searchTerm = '';
  loading = false;

  displayedColumns: string[] = ['nome', 'cpf', 'telefone', 'email', 'acoes'];

  constructor(
    private pacienteService: PacienteService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.carregarPacientes();
  }

  carregarPacientes(): void {
    this.loading = true;
    const request = this.searchTerm 
      ? this.pacienteService.buscarPacientes(this.searchTerm, this.currentPage, this.pageSize)
      : this.pacienteService.listarPacientes(this.currentPage, this.pageSize);

    request.subscribe({
      next: (page: PacientePage) => {
        this.pacientes = page.content;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Erro ao carregar pacientes', 'Fechar', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  buscar(): void {
    this.currentPage = 0;
    this.carregarPacientes();
  }

  limparBusca(): void {
    this.searchTerm = '';
    this.currentPage = 0;
    this.carregarPacientes();
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarPacientes();
  }

  novoPaciente(): void {
    const dialogRef = this.dialog.open(PacienteFormComponent, {
      width: '600px',
      data: { paciente: null }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarPacientes();
      }
    });
  }

  editarPaciente(paciente: Paciente): void {
    const dialogRef = this.dialog.open(PacienteFormComponent, {
      width: '600px',
      data: { paciente }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carregarPacientes();
      }
    });
  }

  excluirPaciente(paciente: Paciente): void {
    if (confirm(`Deseja excluir o paciente ${paciente.nomeCompleto}?`)) {
      this.pacienteService.excluirPaciente(paciente.id!).subscribe({
        next: () => {
          this.snackBar.open('Paciente excluído com sucesso', 'Fechar', { duration: 3000 });
          this.carregarPacientes();
        },
        error: () => {
          this.snackBar.open('Erro ao excluir paciente', 'Fechar', { duration: 3000 });
        }
      });
    }
  }
}