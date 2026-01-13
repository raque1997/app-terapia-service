package com.centroterapia.controller;

import com.centroterapia.model.Material;
import com.centroterapia.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "http://localhost:5173")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<Material>> getAllMateriales() {
        return ResponseEntity.ok(materialService.getAllMateriales());
    }

    @GetMapping("/publicos")
    public ResponseEntity<List<Material>> getMaterialesPublicos() {
        return ResponseEntity.ok(materialService.getMaterialesPublicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return materialService.getMaterialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Material>> getMaterialesByCategoria(
            @PathVariable String categoria) {
        try {
            Material.Categoria cat = Material.Categoria.valueOf(categoria.toUpperCase());
            return ResponseEntity.ok(materialService.getMaterialesByCategoria(cat));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Material> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "visiblePublico", defaultValue = "true") Boolean visiblePublico) {
        try {
            Material material = new Material();
            material.setTitulo(titulo);
            material.setDescripcion(descripcion);
            material.setCategoria(Material.Categoria.valueOf(categoria.toUpperCase()));
            material.setVisiblePublico(visiblePublico);

            Material nuevoMaterial = materialService.uploadMaterial(material, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMaterial);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarMaterial(@PathVariable Long id) {
        try {
            Material material = materialService.getMaterialById(id)
                    .orElseThrow(() -> new RuntimeException("Material no encontrado"));

            Path filePath = Paths.get(material.getRutaArchivo());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + material.getNombreArchivo() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, material.getTipoArchivo())
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Long id,
            @RequestBody Material material) {
        return materialService.updateMaterial(id, material)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        if (materialService.deleteMaterial(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

