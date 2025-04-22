package com.practica.practica.repository;

import com.practica.practica.model.Libro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LibroRepository extends JpaRepository<Libro,Integer> {

    Page<Libro> findByTituloContains(@Param("contiene") String contiene, Pageable pageable);

}

