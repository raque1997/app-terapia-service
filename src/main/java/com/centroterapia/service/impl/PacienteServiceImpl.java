package com.centroterapia.service.impl;

import com.centroterapia.model.Paciente;
import com.centroterapia.repository.PacienteRepository;
import com.centroterapia.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente createPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
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

    @Override
    public boolean deletePaciente(Long id) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    pacienteRepository.delete(paciente);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Paciente> searchPacientes(String query) {
        return pacienteRepository.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                query, query);
    }
}
