package com.centroterapia.service.impl;

import com.centroterapia.model.entity.CitaEntity;
import com.centroterapia.model.entity.PacienteEntity;
import com.centroterapia.model.entity.UsuarioEntity;
import com.centroterapia.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailServiceImpl emailService;

    public List<CitaEntity> getAllCitasServiceImpl() {
        return citaRepository.findAll();
    }

    public Optional<CitaEntity> getCitaById(Long id) {
        return citaRepository.findById(id);
    }

    public List<CitaEntity> getCitasByPaciente(PacienteEntity paciente) {
        return citaRepository.findByPaciente(paciente);
    }

    public List<CitaEntity> getCitasByTerapeuta(UsuarioEntity terapeuta) {
        return citaRepository.findByTerapeuta(terapeuta);
    }

    public List<CitaEntity> getProximasCitas() {
        return citaRepository.findProximasCitas(LocalDateTime.now());
    }

    public CitaEntity createCita(CitaEntity cita) {
        // Verificar disponibilidad
        if (cita.getTerapeuta() != null && 
            citaRepository.existsByTerapeutaAndFechaHoraAndEstadoNot(
                cita.getTerapeuta(), cita.getFechaHora(), CitaEntity.EstadoCita.CANCELADA)) {
            throw new RuntimeException("El horario ya está ocupado");
        }

        CitaEntity nuevaCita = citaRepository.save(cita);

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

    public Optional<CitaEntity> updateCita(Long id, CitaEntity citaDetails) {
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

    public List<CitaEntity> getCitasByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaHoraBetween(inicio, fin);
    }

    public boolean verificarDisponibilidad(UsuarioEntity terapeuta, LocalDateTime fechaHora) {
        return !citaRepository.existsByTerapeutaAndFechaHoraAndEstadoNot(
                terapeuta, fechaHora, CitaEntity.EstadoCita.CANCELADA);
    }
}
