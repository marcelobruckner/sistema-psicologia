import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario, UsuarioRequest, Role } from '../../../models/usuario.model';

@Component({
  selector: 'app-usuario-form',
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.scss']
})
export class UsuarioFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  loading = false;
  roles = Object.values(Role);

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<UsuarioFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { usuario: Usuario | null }
  ) {
    this.isEdit = !!data.usuario;
    this.form = this.createForm();
  }

  ngOnInit(): void {
    if (this.isEdit && this.data.usuario) {
      this.form.patchValue({
        ...this.data.usuario,
        password: '' // Não preencher senha na edição
      });
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: [this.isEdit ? '' : '', this.isEdit ? [] : [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      nomeCompleto: ['', [Validators.required, Validators.minLength(2)]],
      role: [Role.PSICOLOGO, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const usuarioData: UsuarioRequest = this.form.value;

      const request = this.isEdit
        ? this.usuarioService.atualizarUsuario(this.data.usuario!.id!, usuarioData)
        : this.usuarioService.criarUsuario(usuarioData);

      request.subscribe({
        next: () => {
          const message = this.isEdit ? 'Usuário atualizado com sucesso' : 'Usuário criado com sucesso';
          this.snackBar.open(message, 'Fechar', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: (error) => {
          const message = error.error?.message || 'Erro ao salvar usuário';
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
    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength']?.requiredLength;
      return `Deve ter pelo menos ${minLength} caracteres`;
    }
    return '';
  }

  getRoleLabel(role: Role): string {
    return role === Role.ADMIN ? 'Administrador' : 'Psicólogo';
  }
}