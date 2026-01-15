package com.centroterapia.service;

import com.centroterapia.model.entity.PacienteEntity;

import java.util.List;
import java.util.Optional;

public interface PacienteService {
    List<PacienteEntity> getAllPacientes();
    Optional<PacienteEntity> getPacienteById(Long id);
    PacienteEntity createPaciente(PacienteEntity paciente);
    Optional<PacienteEntity> updatePaciente(Long id, PacienteEntity pacienteDetails);
    boolean deletePaciente(Long id);
    List<PacienteEntity> searchPacientes(String query);
}
