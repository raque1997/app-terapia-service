package com.centroterapia.service;

import com.centroterapia.model.entity.CitaEntity;

public interface EmailService {
    void enviarConfirmacionCita(CitaEntity cita);
    void enviarNotificacionCancelacion(CitaEntity cita);
}

