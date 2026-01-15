package com.centroterapia.impl;

import com.centroterapia.model.entity.HorarioEntity;
import com.centroterapia.model.Usuario;
import com.centroterapia.repository.HorarioRepository;
import com.centroterapia.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<HorarioEntity> getAllHorarios() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<HorarioEntity> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }

    @Override
    public List<HorarioEntity> getHorariosDisponibles() {
        return horarioRepository.findByDisponibleTrue();
    }

    @Override
    public List<HorarioEntity> getHorariosByDia(DayOfWeek dia) {
        return horarioRepository.findByDiaSemanaAndDisponibleTrue(dia);
    }

    @Override
    public List<HorarioEntity> getHorariosByTerapeuta(Usuario terapeuta) {
        return horarioRepository.findByTerapeuta(terapeuta);
    }

    @Override
    public HorarioEntity createHorario(HorarioEntity horario) {
        return horarioRepository.save(horario);
    }

    @Override
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

    @Override
    public boolean deleteHorario(Long id) {
        return horarioRepository.findById(id)
                .map(horario -> {
                    horarioRepository.delete(horario);
                    return true;
                })
                .orElse(false);
    }
}
