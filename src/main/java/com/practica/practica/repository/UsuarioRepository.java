package com.practica.practica.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.practica.practica.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {

    Page<Usuario> findByNombreStartsWith(@Param("empieza_con") String startsWith, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE usuarios SET fin_penalizacion=?1 WHERE id=?2",nativeQuery = true)
    void updateFin_penalizacion(Date deadline, int id);


}
