package com.practica.practica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.practica.model.Prestamo;
import com.practica.practica.repository.PrestamoRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class PrestamoService{
    private final PrestamoRepository repository;

    public boolean existePrestamoPorId(int id){
        return repository.existsById(id);
    }

    public Prestamo crearPrestamo(Prestamo prestamo){
        return repository.save(prestamo);
    }

    public Optional<Prestamo> buscarPorId(int id){
        return repository.findById(id);
    }

    public List<Prestamo> getAllPrestamosDeUnLibro(int id){
        return repository.findAllByIdAndFecha_devueltoIsNull(id);
    }

}
