package com.centroterapia.service;

import com.centroterapia.model.Horario;
import com.centroterapia.model.Usuario;
import com.centroterapia.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Horario> getAllHorarios() {
        return horarioRepository.findAll();
    }

    public Optional<Horario> getHorarioById(Long id) {
        return horarioRepository.findById(id);
    }

    public List<Horario> getHorariosDisponibles() {
        return horarioRepository.findByDisponibleTrue();
    }

    public List<Horario> getHorariosByDia(DayOfWeek dia) {
        return horarioRepository.findByDiaSemanaAndDisponibleTrue(dia);
    }

    public List<Horario> getHorariosByTerapeuta(Usuario terapeuta) {
        return horarioRepository.findByTerapeuta(terapeuta);
    }

    public Horario createHorario(Horario horario) {
        return horarioRepository.save(horario);
    }

    public Optional<Horario> updateHorario(Long id, Horario horarioDetails) {
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

