package com.centroterapia.service.impl;

import com.centroterapia.model.entity.MaterialEntity;
import com.centroterapia.repository.MaterialRepository;
import com.centroterapia.service.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    @Override
    public List<MaterialEntity> getAllMateriales() {
        return materialRepository.findAll();
    }

    @Override
    public List<MaterialEntity> getMaterialesPublicos() {
        return materialRepository.findByVisiblePublicoTrue();
    }

    @Override
    public Optional<MaterialEntity> getMaterialById(Long id) {
        return materialRepository.findById(id);
    }

    @Override
    public List<MaterialEntity> getMaterialesByCategoria(MaterialEntity.Categoria categoria) {
        return materialRepository.findByCategoriaAndVisiblePublicoTrue(categoria);
    }

    @Override
    public List<MaterialEntity> searchMateriales(String query) {
        return materialRepository.findByTituloContainingIgnoreCase(query);
    }

    @Override
    public MaterialEntity uploadMaterial(MaterialEntity material, MultipartFile file) throws IOException {
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

    @Override
    public Optional<MaterialEntity> updateMaterial(Long id, MaterialEntity materialDetails) {
        return materialRepository.findById(id)
                .map(material -> {
                    material.setTitulo(materialDetails.getTitulo());
                    material.setDescripcion(materialDetails.getDescripcion());
                    material.setCategoria(materialDetails.getCategoria());
                    material.setVisiblePublico(materialDetails.getVisiblePublico());
                    return materialRepository.save(material);
                });
    }

    @Override
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
