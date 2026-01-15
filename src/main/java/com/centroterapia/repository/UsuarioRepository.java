package com.centroterapia.repository;

import com.centroterapia.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    
    Optional<UsuarioEntity> findByUsername(String username);
    
    Optional<UsuarioEntity> findByEmail(String email);
    
    List<UsuarioEntity> findByRol(UsuarioEntity.Rol rol);
    
    List<UsuarioEntity> findByActivoTrue();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}

