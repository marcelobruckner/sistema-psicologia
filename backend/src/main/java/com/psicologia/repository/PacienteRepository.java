package com.psicologia.repository;

import com.psicologia.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    
    List<Paciente> findByAtivoTrue();
    
    Page<Paciente> findByAtivoTrue(Pageable pageable);
    
    Optional<Paciente> findByCpf(String cpf);
    
    @Query("SELECT p FROM Paciente p WHERE p.ativo = true AND " +
           "(LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.cpf) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :termo, '%')))")
    Page<Paciente> buscarPorTermo(@Param("termo") String termo, Pageable pageable);
    
    boolean existsByCpf(String cpf);
}