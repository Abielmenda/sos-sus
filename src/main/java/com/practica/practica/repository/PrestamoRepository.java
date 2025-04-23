
package com.practica.practica.repository;

import com.practica.practica.model.Prestamo;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


public interface PrestamoRepository extends JpaRepository<Prestamo,Integer> {

    @Query(value = "SELECT * from prestamos WHERE id_libro=?1 AND fecha_devuelto IS NULL",nativeQuery = true)
    List<Prestamo> findAllByIdAndFecha_devueltoIsNull(int id);

    @Query(value = "SELECT * from prestamos WHERE id_usuario=?1 AND fecha_devuelto IS NULL AND fecha_prestado BETWEEN ?2 AND ?3  ORDER BY fecha_prestado ASC",nativeQuery = true)
    List<Prestamo> findActivosByIdUsuario(int id,Date start, Date end);

    @Query(value = "SELECT * from prestamos WHERE id_usuario=?1 AND fecha_devuelto IS NOT NULL AND fecha_prestado BETWEEN ?2 AND ?3 ORDER BY fecha_devuelto DESC LIMIT 5",nativeQuery = true)
    List<Prestamo> findDevueltosByIdUsuario(int id,Date start, Date end);


}
