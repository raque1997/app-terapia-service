package com.centroterapia.service;

import com.centroterapia.model.Cita;
import com.centroterapia.model.Paciente;
import com.centroterapia.model.Usuario;
import com.centroterapia.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;

    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> getCitaById(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> getCitasByPaciente(Paciente paciente) {
        return citaRepository.findByPaciente(paciente);
    }

    public List<Cita> getCitasByTerapeuta(Usuario terapeuta) {
        return citaRepository.findByTerapeuta(terapeuta);
    }

    public List<Cita> getProximasCitas() {
        return citaRepository.findProximasCitas(LocalDateTime.now());
    }

    public Cita createCita(Cita cita) {
        // Verificar disponibilidad
        if (cita.getTerapeuta() != null && 
            citaRepository.existsByTerapeutaAndFechaHoraAndEstadoNot(
                cita.getTerapeuta(), cita.getFechaHora(), Cita.EstadoCita.CANCELADA)) {
            throw new RuntimeException("El horario ya está ocupado");
        }

        Cita nuevaCita = citaRepository.save(cita);

        // Enviar email de confirmación
        if (cita.getEmailConfirmacion() != null && !cita.getEmailConfirmacion().isEmpty()) {
            try {
                emailService.enviarConfirmacionCita(nuevaCita);
                nuevaCita.setConfirmacionEnviada(true);
                citaRepository.save(nuevaCita);
            } catch (Exception e) {
                // Log error pero no fallar la creación de la cita
                System.err.println("Error al enviar email de confirmación: " + e.getMessage());
            }
        }

        return nuevaCita;
    }

    public Optional<Cita> updateCita(Long id, Cita citaDetails) {
        return citaRepository.findById(id)
                .map(cita -> {
                    cita.setPaciente(citaDetails.getPaciente());
                    cita.setTerapeuta(citaDetails.getTerapeuta());
                    cita.setFechaHora(citaDetails.getFechaHora());
                    cita.setDuracionMinutos(citaDetails.getDuracionMinutos());
                    cita.setEstado(citaDetails.getEstado());
                    cita.setMotivo(citaDetails.getMotivo());
                    cita.setObservaciones(citaDetails.getObservaciones());
                    return citaRepository.save(cita);
                });
    }

    public boolean deleteCita(Long id) {
        return citaRepository.findById(id)
                .map(cita -> {
                    citaRepository.delete(cita);
                    return true;
                })
                .orElse(false);
    }

    public List<Cita> getCitasByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaHoraBetween(inicio, fin);
    }

    public boolean verificarDisponibilidad(Usuario terapeuta, LocalDateTime fechaHora) {
        return !citaRepository.existsByTerapeutaAndFechaHoraAndEstadoNot(
                terapeuta, fechaHora, Cita.EstadoCita.CANCELADA);
    }
}

