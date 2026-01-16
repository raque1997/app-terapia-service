package com.centroterapia.service.impl;

import com.centroterapia.model.entity.HorarioEntity;
import com.centroterapia.model.entity.UsuarioEntity;
import com.centroterapia.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioServiceImpl {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<HorarioEntity> getAllHorarios() {
        return horarioRepository.findAll();
    }

    public Optional<HorarioEntity> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }

    public List<HorarioEntity> getHorariosDisponibles() {
        return horarioRepository.findByDisponibleTrue();
    }

    public List<HorarioEntity> getHorariosByDia(DayOfWeek dia) {
        return horarioRepository.findByDiaSemanaAndDisponibleTrue(dia);
    }

    public List<HorarioEntity> getHorariosByTerapeuta(UsuarioEntity terapeuta) {
        return horarioRepository.findByTerapeuta(terapeuta);
    }

    public HorarioEntity createHorario(HorarioEntity horario) {
        return horarioRepository.save(horario);
    }

    public Optional<HorarioEntity> updateHorario(Long id, HorarioEntity horarioDetails) {
        return horarioRepository.findById(id)
                .map(horario -> {
                    horario.setDiaSemana(horarioDetails.getDiaSemana());
                    horario.setHoraInicio(horarioDetails.getHoraInicio());
                    horario.setHoraFin(horarioDetails.getHoraFin());
                    horario.setDisponible(horarioDetails.getDisponible());
                    horario.setTerapeuta(horarioDetails.getTerapeuta());
                    horario.setDuracionSesion(horarioDetails.getDuracionSesion());
                    return horarioRepository.save(horario);
                });
    }

    public boolean deleteHorario(Long id) {
        return horarioRepository.findById(id)
                .map(horario -> {
                    horarioRepository.delete(horario);
                    return true;
                })
                .orElse(false);
    }
}
