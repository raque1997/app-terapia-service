package com.centroterapia.controller;

import com.centroterapia.model.entity.HorarioEntity;
import com.centroterapia.service.HorarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "http://localhost:5173")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping
    public ResponseEntity<List<HorarioEntity>> getAllHorarios() {
        return ResponseEntity.ok(horarioService.getAllHorarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioEntity> getHorarioById(@PathVariable Long id) {
        return horarioService.getHorarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<HorarioEntity>> getHorariosDisponibles() {
        return ResponseEntity.ok(horarioService.getHorariosDisponibles());
    }

    @GetMapping("/dia/{dia}")
    public ResponseEntity<List<HorarioEntity>> getHorariosByDia(@PathVariable String dia) {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(dia.toUpperCase());
            return ResponseEntity.ok(horarioService.getHorariosByDia(dayOfWeek));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<HorarioEntity> createHorario(@Valid @RequestBody HorarioEntity horario) {
        HorarioEntity nuevoHorario = horarioService.createHorario(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHorario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioEntity> updateHorario(
            @PathVariable Long id,
            @Valid @RequestBody HorarioEntity horario) {
        return horarioService.updateHorario(id, horario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        if (horarioService.deleteHorario(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

