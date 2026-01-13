package com.centroterapia.repository;

import com.centroterapia.model.Cita;
import com.centroterapia.model.Paciente;
import com.centroterapia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    List<Cita> findByPaciente(Paciente paciente);
    
    List<Cita> findByTerapeuta(Usuario terapeuta);
    
    List<Cita> findByEstado(Cita.EstadoCita estado);
    
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT c FROM Cita c WHERE c.terapeuta = :terapeuta " +
           "AND c.fechaHora BETWEEN :inicio AND :fin " +
           "AND c.estado != 'CANCELADA'")
    List<Cita> findCitasByTerapeutaAndFecha(
            @Param("terapeuta") Usuario terapeuta,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT c FROM Cita c WHERE c.fechaHora >= :hoy " +
           "AND c.estado = 'PENDIENTE' ORDER BY c.fechaHora ASC")
    List<Cita> findProximasCitas(@Param("hoy") LocalDateTime hoy);
    
    boolean existsByTerapeutaAndFechaHoraAndEstadoNot(
            Usuario terapeuta, LocalDateTime fechaHora, Cita.EstadoCita estado);
}

