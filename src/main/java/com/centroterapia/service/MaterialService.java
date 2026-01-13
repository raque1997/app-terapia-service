package com.centroterapia.service;

import com.centroterapia.model.Material;
import com.centroterapia.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    public List<Material> getAllMateriales() {
        return materialRepository.findAll();
    }

    public List<Material> getMaterialesPublicos() {
        return materialRepository.findByVisiblePublicoTrue();
    }

    public Optional<Material> getMaterialById(Long id) {
        return materialRepository.findById(id);
    }

    public List<Material> getMaterialesByCategoria(Material.Categoria categoria) {
        return materialRepository.findByCategoriaAndVisiblePublicoTrue(categoria);
    }

    public List<Material> searchMateriales(String query) {
        return materialRepository.findByTituloContainingIgnoreCase(query);
    }

    public Material uploadMaterial(Material material, MultipartFile file) throws IOException {
        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreUnico = UUID.randomUUID().toString() + extension;

        // Guardar archivo
        Path filePath = uploadPath.resolve(nombreUnico);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Configurar material
        material.setNombreArchivo(nombreOriginal);
        material.setRutaArchivo(filePath.toString());
        material.setTipoArchivo(file.getContentType());
        material.setTamanoBytes(file.getSize());

        return materialRepository.save(material);
    }

    public Optional<Material> updateMaterial(Long id, Material materialDetails) {
        return materialRepository.findById(id)
                .map(material -> {
                    material.setTitulo(materialDetails.getTitulo());
                    material.setDescripcion(materialDetails.getDescripcion());
                    material.setCategoria(materialDetails.getCategoria());
                    material.setVisiblePublico(materialDetails.getVisiblePublico());
                    return materialRepository.save(material);
                });
    }

    public boolean deleteMaterial(Long id) {
        return materialRepository.findById(id)
                .map(material -> {
                    // Eliminar archivo físico
                    try {
                        Path filePath = Paths.get(material.getRutaArchivo());
                        Files.deleteIfExists(filePath);
                    } catch (IOException e) {
                        System.err.println("Error al eliminar archivo: " + e.getMessage());
                    }
                    
                    materialRepository.delete(material);
                    return true;
                })
                .orElse(false);
    }
}

