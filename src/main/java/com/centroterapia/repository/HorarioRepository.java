package com.centroterapia.repository;

import com.centroterapia.model.Horario;
import com.centroterapia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    
    List<Horario> findByDiaSemana(DayOfWeek diaSemana);
    
    List<Horario> findByTerapeuta(Usuario terapeuta);
    
    List<Horario> findByDisponibleTrue();
    
    List<Horario> findByDiaSemanaAndDisponibleTrue(DayOfWeek diaSemana);
}

