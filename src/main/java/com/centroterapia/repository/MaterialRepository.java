package com.centroterapia.repository;

import com.centroterapia.model.entity.MaterialEntity;
import com.centroterapia.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {
    
    List<MaterialEntity> findByCategoria(MaterialEntity.Categoria categoria);
    
    List<MaterialEntity> findBySubidoPor(UsuarioEntity usuario);
    
    List<MaterialEntity> findByVisiblePublicoTrue();
    
    List<MaterialEntity> findByTituloContainingIgnoreCase(String titulo);
    
    List<MaterialEntity> findByCategoriaAndVisiblePublicoTrue(MaterialEntity.Categoria categoria);
}

