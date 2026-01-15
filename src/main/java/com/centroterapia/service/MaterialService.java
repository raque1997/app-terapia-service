package com.centroterapia.service;

import com.centroterapia.model.entity.MaterialEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MaterialService {
    List<MaterialEntity> getAllMateriales();
    List<MaterialEntity> getMaterialesPublicos();
    Optional<MaterialEntity> getMaterialById(Long id);
    List<MaterialEntity> getMaterialesByCategoria(MaterialEntity.Categoria categoria);
    List<MaterialEntity> searchMateriales(String query);
    MaterialEntity uploadMaterial(MaterialEntity material, MultipartFile file) throws IOException;
    Optional<MaterialEntity> updateMaterial(Long id, MaterialEntity materialDetails);
    boolean deleteMaterial(Long id);
}

