package com.centroterapia.repository;

import com.centroterapia.model.entity.HorarioEntity;
import com.centroterapia.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioEntity, Long> {
    
    List<HorarioEntity> findByDiaSemana(DayOfWeek diaSemana);
    
    List<HorarioEntity> findByTerapeuta(UsuarioEntity terapeuta);
    
    List<HorarioEntity> findByDisponibleTrue();
    
    List<HorarioEntity> findByDiaSemanaAndDisponibleTrue(DayOfWeek diaSemana);
}

