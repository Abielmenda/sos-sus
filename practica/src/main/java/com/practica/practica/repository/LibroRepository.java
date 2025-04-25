package com.practica.practica.repository;

import com.practica.practica.model.Libro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Integer> {

    Page<Libro> findByTituloContains(@Param("contiene") String contiene, Pageable pageable);
    Page<Libro> findByCopiasGreaterThan(int min, Pageable pageable);

    Page<Libro> findByTituloContainingAndCopiasGreaterThan(
        @Param("contiene") String contiene, 
        @Param("min") int min, 
        Pageable pageable
    );
}

