package com.centroterapia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class HealthCheckController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Environment env;

    @GetMapping("/health-check")
    public Object healthCheck() {
        return Map.of("message", "Service Up, profile = " + env.getProperty("spring.profiles.active"));
    }

    @GetMapping("/db")
    public ResponseEntity<String> testDbConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return ResponseEntity.ok("Conexión a Base de Datos EXITOSA: " + connection.getMetaData().getURL());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("FALLO conexión a Base de Datos: " + e.getMessage());
        }
    }
}

