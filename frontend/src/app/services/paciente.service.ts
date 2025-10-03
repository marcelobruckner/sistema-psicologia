import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Paciente, PacienteRequest, PacientePage } from '../models/paciente.model';

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private apiUrl = 'http://localhost:8080/api/pacientes';

  constructor(private http: HttpClient) {}

  listarPacientes(page: number = 0, size: number = 10): Observable<PacientePage> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PacientePage>(this.apiUrl, { params });
  }

  buscarPacientes(termo: string, page: number = 0, size: number = 10): Observable<PacientePage> {
    const params = new HttpParams()
      .set('termo', termo)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PacientePage>(`${this.apiUrl}/buscar`, { params });
  }

  obterPaciente(id: string): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.apiUrl}/${id}`);
  }

  criarPaciente(paciente: PacienteRequest): Observable<Paciente> {
    return this.http.post<Paciente>(this.apiUrl, paciente);
  }

  atualizarPaciente(id: string, paciente: PacienteRequest): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.apiUrl}/${id}`, paciente);
  }

  excluirPaciente(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}