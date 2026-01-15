package com.centroterapia.service;

import com.centroterapia.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteService {
    List<Paciente> getAllPacientes();
    Optional<Paciente> getPacienteById(Long id);
    Paciente createPaciente(Paciente paciente);
    Optional<Paciente> updatePaciente(Long id, Paciente pacienteDetails);
    boolean deletePaciente(Long id);
    List<Paciente> searchPacientes(String query);
}
