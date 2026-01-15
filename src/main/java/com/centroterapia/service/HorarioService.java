package com.centroterapia.service;

import com.centroterapia.model.entity.HorarioEntity;
import com.centroterapia.model.Usuario;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface HorarioService {
    List<HorarioEntity> getAllHorarios();
    Optional<HorarioEntity> getHorarioById(Long id);
    List<HorarioEntity> getHorariosDisponibles();
    List<HorarioEntity> getHorariosByDia(DayOfWeek dia);
    List<HorarioEntity> getHorariosByTerapeuta(Usuario terapeuta);
    HorarioEntity createHorario(HorarioEntity horario);
    Optional<HorarioEntity> updateHorario(Long id, HorarioEntity horarioDetails);
    boolean deleteHorario(Long id);
}

