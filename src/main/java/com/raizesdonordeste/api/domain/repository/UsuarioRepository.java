package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}