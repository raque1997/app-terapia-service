package com.centroterapia.service;

import com.centroterapia.model.entity.CitaEntity;
import com.centroterapia.model.Paciente;
import com.centroterapia.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {
    List<CitaEntity> getAllCitas();
    Optional<CitaEntity> getCitaById(Long id);
    List<CitaEntity> getCitasByPaciente(Paciente paciente);
    List<CitaEntity> getCitasByTerapeuta(Usuario terapeuta);
    List<CitaEntity> getProximasCitas();
    CitaEntity createCita(CitaEntity cita);
    Optional<CitaEntity> updateCita(Long id, CitaEntity citaDetails);
    boolean deleteCita(Long id);
    List<CitaEntity> getCitasByFechaRange(LocalDateTime inicio, LocalDateTime fin);
    boolean verificarDisponibilidad(Usuario terapeuta, LocalDateTime fechaHora);
}

