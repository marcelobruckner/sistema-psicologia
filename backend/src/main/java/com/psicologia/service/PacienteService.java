package com.psicologia.service;

import com.psicologia.dto.PacienteRequest;
import com.psicologia.entity.Paciente;
import com.psicologia.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findByAtivoTrue();
    }

    public Page<Paciente> listarComPaginacao(Pageable pageable) {
        return pacienteRepository.findByAtivoTrue(pageable);
    }

    public Page<Paciente> buscarPorTermo(String termo, Pageable pageable) {
        return pacienteRepository.buscarPorTermo(termo, pageable);
    }

    public Optional<Paciente> buscarPorId(UUID id) {
        return pacienteRepository.findById(id);
    }

    public Paciente salvar(PacienteRequest request) {
        if (request.getCpf() != null && pacienteRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Paciente paciente = new Paciente();
        mapearRequestParaEntity(request, paciente);
        return pacienteRepository.save(paciente);
    }

    public Paciente atualizar(UUID id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        if (request.getCpf() != null && !request.getCpf().equals(paciente.getCpf()) 
            && pacienteRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        mapearRequestParaEntity(request, paciente);
        return pacienteRepository.save(paciente);
    }

    public void excluir(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    private void mapearRequestParaEntity(PacienteRequest request, Paciente paciente) {
        paciente.setNomeCompleto(request.getNomeCompleto());
        paciente.setCpf(request.getCpf());
        paciente.setRg(request.getRg());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setTelefone(request.getTelefone());
        paciente.setEmail(request.getEmail());
        paciente.setEndereco(request.getEndereco());
        paciente.setProfissao(request.getProfissao());
        paciente.setEstadoCivil(request.getEstadoCivil());
        paciente.setContatoEmergencia(request.getContatoEmergencia());
        paciente.setTelefoneEmergencia(request.getTelefoneEmergencia());
        paciente.setObservacoes(request.getObservacoes());
    }
}