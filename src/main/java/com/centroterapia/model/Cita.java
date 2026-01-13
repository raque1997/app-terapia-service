package com.centroterapia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    @NotNull(message = "El paciente es obligatorio")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "terapeuta_id")
    private Usuario terapeuta;

    @Column(name = "fecha_hora", nullable = false)
    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 60;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(length = 500)
    private String motivo;

    @Column(length = 1000)
    private String observaciones;

    @Column(name = "email_confirmacion")
    private String emailConfirmacion;

    @Column(name = "confirmacion_enviada")
    private Boolean confirmacionEnviada = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EstadoCita {
        PENDIENTE,
        CONFIRMADA,
        COMPLETADA,
        CANCELADA,
        NO_ASISTIO
    }
}

