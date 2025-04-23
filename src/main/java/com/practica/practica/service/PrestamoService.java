package com.practica.practica.service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

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

    public List<Prestamo> buscarPrestamosActivosDeUsuario(int id,Date start, Date end){
        return repository.findActivosByIdUsuario(id,start,end);
    }

    public List<Prestamo> buscarPrestamosDevueltosDeUsuario(int id,Date start, Date end){
        return repository.findDevueltosByIdUsuario(id,start,end);
    }

    public List<Prestamo> buscarTodosPrestamosDevueltosDeUsuario(int id,Date start, Date end){
        return repository.findAllDevueltosByIdUsuario(id,start,end);
    }


    public List<Prestamo> getAllPrestamosDeUnLibro(int id){
        return repository.findAllByIdAndFecha_devueltoIsNull(id);
    }

    public Optional<Prestamo> buscarPorId(int id){
        return repository.findById(id);
    }
}
