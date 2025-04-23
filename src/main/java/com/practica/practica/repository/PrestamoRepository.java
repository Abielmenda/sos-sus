
package com.practica.practica.repository;

import com.practica.practica.model.Prestamo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


public interface PrestamoRepository extends JpaRepository<Prestamo,Integer> {

    @Query(value = "SELECT * from prestamos WHERE id_libro=?1 AND fecha_devuelto IS NULL",nativeQuery = true)
    List<Prestamo> findAllByIdAndFecha_devueltoIsNull(int id);
}
