package com.centroterapia.service;

import com.centroterapia.model.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@centroterapia.com}")
    private String fromEmail;

    private static final DateTimeFormatter formatter = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void enviarConfirmacionCita(Cita cita) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(cita.getEmailConfirmacion());
        message.setSubject("Confirmación de Cita - Centro de Terapia Infantil");
        
        String contenido = String.format(
            "Estimado/a,\n\n" +
            "Su cita ha sido agendada exitosamente.\n\n" +
            "Detalles de la cita:\n" +
            "- Paciente: %s %s\n" +
            "- Fecha y hora: %s\n" +
            "- Duración: %d minutos\n" +
            "- Motivo: %s\n\n" +
            "Por favor, llegue 10 minutos antes de la hora programada.\n\n" +
            "Si necesita cancelar o reagendar, por favor contacte con nosotros.\n\n" +
            "Saludos cordiales,\n" +
            "Centro de Terapia Infantil",
            cita.getPaciente().getNombre(),
            cita.getPaciente().getApellidos(),
            cita.getFechaHora().format(formatter),
            cita.getDuracionMinutos(),
            cita.getMotivo() != null ? cita.getMotivo() : "No especificado"
        );
        
        message.setText(contenido);
        
        mailSender.send(message);
    }

    public void enviarNotificacionCancelacion(Cita cita) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(cita.getEmailConfirmacion());
        message.setSubject("Cancelación de Cita - Centro de Terapia Infantil");
        
        String contenido = String.format(
            "Estimado/a,\n\n" +
            "Le informamos que su cita ha sido cancelada.\n\n" +
            "Detalles de la cita cancelada:\n" +
            "- Paciente: %s %s\n" +
            "- Fecha y hora: %s\n\n" +
            "Si desea reagendar, por favor contacte con nosotros.\n\n" +
            "Saludos cordiales,\n" +
            "Centro de Terapia Infantil",
            cita.getPaciente().getNombre(),
            cita.getPaciente().getApellidos(),
            cita.getFechaHora().format(formatter)
        );
        
        message.setText(contenido);
        
        mailSender.send(message);
    }
}

