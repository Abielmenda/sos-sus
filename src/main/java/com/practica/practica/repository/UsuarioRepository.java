package com.practica.practica.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.practica.practica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    Page<Usuario> findByNombreStartsWith(@Param("empieza_con") String startsWith, Pageable pageable);

}
