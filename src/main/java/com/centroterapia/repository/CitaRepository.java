package com.centroterapia.repository;

import com.centroterapia.model.entity.CitaEntity;
import com.centroterapia.model.Paciente;
import com.centroterapia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
    
    List<CitaEntity> findByPaciente(Paciente paciente);
    
    List<CitaEntity> findByTerapeuta(Usuario terapeuta);
    
    List<CitaEntity> findByEstado(CitaEntity.EstadoCita estado);
    
    List<CitaEntity> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT c FROM CitaEntity c WHERE c.terapeuta = :terapeuta " +
           "AND c.fechaHora BETWEEN :inicio AND :fin " +
           "AND c.estado != 'CANCELADA'")
    List<CitaEntity> findCitasByTerapeutaAndFecha(
            @Param("terapeuta") Usuario terapeuta,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT c FROM CitaEntity c WHERE c.fechaHora >= :hoy " +
           "AND c.estado = 'PENDIENTE' ORDER BY c.fechaHora ASC")
    List<CitaEntity> findProximasCitas(@Param("hoy") LocalDateTime hoy);
    
    boolean existsByTerapeutaAndFechaHoraAndEstadoNot(
            Usuario terapeuta, LocalDateTime fechaHora, CitaEntity.EstadoCita estado);
}

