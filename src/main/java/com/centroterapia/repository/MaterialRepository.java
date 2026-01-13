package com.centroterapia.repository;

import com.centroterapia.model.Material;
import com.centroterapia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    List<Material> findByCategoria(Material.Categoria categoria);
    
    List<Material> findBySubidoPor(Usuario usuario);
    
    List<Material> findByVisiblePublicoTrue();
    
    List<Material> findByTituloContainingIgnoreCase(String titulo);
    
    List<Material> findByCategoriaAndVisiblePublicoTrue(Material.Categoria categoria);
}

