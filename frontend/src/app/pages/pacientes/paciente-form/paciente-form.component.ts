import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PacienteService } from '../../../services/paciente.service';
import { Paciente, PacienteRequest } from '../../../models/paciente.model';

@Component({
  selector: 'app-paciente-form',
  templateUrl: './paciente-form.component.html',
  styleUrls: ['./paciente-form.component.scss']
})
export class PacienteFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private pacienteService: PacienteService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<PacienteFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { paciente: Paciente | null }
  ) {
    try {
      this.isEdit = !!(data?.paciente);
      this.form = this.createForm();
    } catch (error) {
      console.error('Erro ao inicializar formulário:', error);
      this.snackBar.open('Erro ao carregar formulário', 'Fechar', { duration: 3000 });
      this.dialogRef.close(false);
    }
  }

  ngOnInit(): void {
    if (this.isEdit && this.data.paciente) {
      this.form.patchValue(this.data.paciente);
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      nomeCompleto: ['', [Validators.required, Validators.minLength(2)]],
      cpf: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
      rg: [''],
      dataNascimento: ['', Validators.required],
      telefone: ['', [Validators.required, Validators.pattern(/^\d{10,11}$/)]],
      email: ['', Validators.email],
      endereco: [''],
      profissao: [''],
      estadoCivil: [''],
      contatoEmergencia: [''],
      telefoneEmergencia: ['', Validators.pattern(/^\d{10,11}$/)],
      observacoes: ['']
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const pacienteData: PacienteRequest = this.form.value;

      const request = this.isEdit
        ? this.pacienteService.atualizarPaciente(this.data.paciente!.id!, pacienteData)
        : this.pacienteService.criarPaciente(pacienteData);

      request.subscribe({
        next: () => {
          const message = this.isEdit ? 'Paciente atualizado com sucesso' : 'Paciente criado com sucesso';
          this.snackBar.open(message, 'Fechar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => {
          const message = error.error?.message || 'Erro ao salvar paciente';
          this.snackBar.open(message, 'Fechar', { duration: 3000 });
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  getErrorMessage(field: string): string {
    const control = this.form.get(field);
    if (control?.hasError('required')) {
      return 'Campo obrigatório';
    }
    if (control?.hasError('email')) {
      return 'Email inválido';
    }
    if (control?.hasError('pattern')) {
      if (field === 'cpf') return 'CPF deve ter 11 dígitos';
      if (field === 'telefone' || field === 'telefoneEmergencia') return 'Telefone deve ter 10 ou 11 dígitos';
    }
    if (control?.hasError('minlength')) {
      return 'Nome deve ter pelo menos 2 caracteres';
    }
    return '';
  }
}