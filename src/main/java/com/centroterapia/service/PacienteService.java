package com.centroterapia.service;

import com.centroterapia.model.Paciente;
import com.centroterapia.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }

    public Paciente createPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> updatePaciente(Long id, Paciente pacienteDetails) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.setNombre(pacienteDetails.getNombre());
                    paciente.setApellidos(pacienteDetails.getApellidos());
                    paciente.setFechaNacimiento(pacienteDetails.getFechaNacimiento());
                    paciente.setEmail(pacienteDetails.getEmail());
                    paciente.setTelefono(pacienteDetails.getTelefono());
                    paciente.setDiagnostico(pacienteDetails.getDiagnostico());
                    paciente.setNombreTutor(pacienteDetails.getNombreTutor());
                    paciente.setTelefonoTutor(pacienteDetails.getTelefonoTutor());
                    return pacienteRepository.save(paciente);
                });
    }

    public boolean deletePaciente(Long id) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    pacienteRepository.delete(paciente);
                    return true;
                })
                .orElse(false);
    }

    public List<Paciente> searchPacientes(String query) {
        return pacienteRepository.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                query, query);
    }
}

