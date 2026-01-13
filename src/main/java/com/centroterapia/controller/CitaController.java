package com.centroterapia.controller;

import com.centroterapia.model.Cita;
import com.centroterapia.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> getAllCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable Long id) {
        return citaService.getCitaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<Cita>> getProximasCitas() {
        return ResponseEntity.ok(citaService.getProximasCitas());
    }

    @GetMapping("/rango")
    public ResponseEntity<List<Cita>> getCitasByRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(citaService.getCitasByFechaRange(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<?> createCita(@Valid @RequestBody Cita cita) {
        try {
            Cita nuevaCita = citaService.createCita(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
        } catch (Exception e) {
            e.printStackTrace(); // Log del error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear cita: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(
            @PathVariable Long id,
            @Valid @RequestBody Cita cita) {
        return citaService.updateCita(id, cita)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        if (citaService.deleteCita(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
