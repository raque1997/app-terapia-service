package com.centroterapia.impl;

import com.centroterapia.model.entity.CitaEntity;
import com.centroterapia.model.Paciente;
import com.centroterapia.model.Usuario;
import com.centroterapia.repository.CitaRepository;
import com.centroterapia.service.CitaService;
import com.centroterapia.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<CitaEntity> getAllCitas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<CitaEntity> getCitaById(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    public List<CitaEntity> getCitasByPaciente(Paciente paciente) {
        return citaRepository.findByPaciente(paciente);
    }

    @Override
    public List<CitaEntity> getCitasByTerapeuta(Usuario terapeuta) {
        return citaRepository.findByTerapeuta(terapeuta);
    }

    @Override
    public List<CitaEntity> getProximasCitas() {
        return citaRepository.findProximasCitas(LocalDateTime.now());
    }

    @Override
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

    @Override
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

    @Override
    public boolean deleteCita(Long id) {
        return citaRepository.findById(id)
                .map(cita -> {
                    citaRepository.delete(cita);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<CitaEntity> getCitasByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaHoraBetween(inicio, fin);
    }

    @Override
    public boolean verificarDisponibilidad(Usuario terapeuta, LocalDateTime fechaHora) {
        return !citaRepository.existsByTerapeutaAndFechaHoraAndEstadoNot(
                terapeuta, fechaHora, CitaEntity.EstadoCita.CANCELADA);
    }
}
