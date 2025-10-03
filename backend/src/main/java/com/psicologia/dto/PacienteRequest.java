package com.psicologia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PacienteRequest {
    
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nomeCompleto;
    
    @Size(max = 14, message = "CPF deve ter no máximo 14 caracteres")
    private String cpf;
    
    @Size(max = 20, message = "RG deve ter no máximo 20 caracteres")
    private String rg;
    
    private LocalDate dataNascimento;
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;
    
    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;
    
    private String endereco;
    
    @Size(max = 100, message = "Profissão deve ter no máximo 100 caracteres")
    private String profissao;
    
    @Size(max = 20, message = "Estado civil deve ter no máximo 20 caracteres")
    private String estadoCivil;
    
    @Size(max = 150, message = "Contato de emergência deve ter no máximo 150 caracteres")
    private String contatoEmergencia;
    
    @Size(max = 20, message = "Telefone de emergência deve ter no máximo 20 caracteres")
    private String telefoneEmergencia;
    
    private String observacoes;

    // Getters e Setters
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getContatoEmergencia() { return contatoEmergencia; }
    public void setContatoEmergencia(String contatoEmergencia) { this.contatoEmergencia = contatoEmergencia; }
    
    public String getTelefoneEmergencia() { return telefoneEmergencia; }
    public void setTelefoneEmergencia(String telefoneEmergencia) { this.telefoneEmergencia = telefoneEmergencia; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}