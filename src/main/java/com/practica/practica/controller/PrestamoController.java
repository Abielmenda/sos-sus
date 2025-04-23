package com.practica.practica.controller;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.practica.practica.model.*;
import com.practica.practica.service.*;
import com.practica.practica.repository.*;
import com.practica.practica.assembler.*;
import com.practica.practica.exceptions.*;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/prestamos")
@AllArgsConstructor // Para generar un constructor con todos los argumentos
//maneja peticiones web
public class PrestamoController{

    private final PrestamoService service;
    private PagedResourcesAssembler<Prestamo> pagedResourcesAssembler;
    private PrestamoModelAssembler usuarioModelAssembler;

    private final LibroService libro_service;
    private final UsuarioService usuario_service;

    @PostMapping()
    ResponseEntity<Void> nuevoPrestamo(@RequestBody Prestamo nuevo){
        Libro book =  libro_service.buscarPorId(nuevo.getId_libro()).orElseThrow(()->new LibroNotFoundException(nuevo.getId_libro()));
        Usuario user =  usuario_service.buscarPorId(nuevo.getId_usuario()).orElseThrow(()->new UsuarioNotFoundException(nuevo.getId_usuario()));
        
        Date penalization = user.getFin_penalizacion();
        Date currentDate = new Date();

        if(penalization != null && currentDate.before(penalization)){
            throw new UsuarioPenalizadoException(nuevo.getId_usuario());
        }

        //Obtener cantidad de pedidos activos con el libro book.id. (Avoid transaction in DB)
        List<Prestamo> cantidad = service.getAllPrestamosDeUnLibro(book.getId());

        if(cantidad.size() >= book.getCopias()){
            throw new LibroNoDisponibleException(book.getId());
        }


        if(!service.existePrestamoPorId(nuevo.getId_prestamo())){
            nuevo.setFecha_prestado(currentDate);
            Prestamo prestamo = service.crearPrestamo(nuevo);
            return ResponseEntity.created(linkTo(PrestamoController.class).slash(nuevo.getId_prestamo()).toUri()).build();
        }
        return null;
    }

    
    @GetMapping(value="/{id}", produces = {"application/json"})
    public ResponseEntity<Prestamo> buscarPrestamo(@PathVariable int id){
        Prestamo prestamo = service.buscarPorId(id).orElseThrow(() -> new PrestamoNotFoundException(id));
        prestamo.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(id)).withSelfRel());
        return ResponseEntity.ok(prestamo);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> devolverPrestamo(@Valid @RequestBody Prestamo newPrestamo, @PathVariable int id){
        Prestamo ret = service.buscarPorId(id).map(
            Prestamo -> {

                Date new_fecha_devuelto = newPrestamo.getFecha_devuelto();
                if(new_fecha_devuelto != null){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(Prestamo.getFecha_prestado()); 
                    cal.add(Calendar.WEEK_OF_YEAR, 2); 
                    Date deadline = cal.getTime();

                    if(new_fecha_devuelto.after(deadline)){
                        usuario_service.penalizar(Prestamo.getId_usuario());
                    }

                    Prestamo.setFecha_devuelto(new_fecha_devuelto);
                }
                return service.crearPrestamo(Prestamo);
            }).orElseThrow(() -> new PrestamoNotFoundException(id));


        return ResponseEntity.ok(ret);
    }


    //Ampliar plazo
    
    
    @PutMapping("/{id}/ampliar")
    public ResponseEntity<Prestamo> ampliarPrestamo(@PathVariable int id){
        Prestamo ret = service.buscarPorId(id).map(
            Prestamo -> {
                Usuario user = usuario_service.buscarPorId(Prestamo.getId_usuario()).orElseThrow(()->new UsuarioNotFoundException(Prestamo.getId_usuario()));
                Date hoy = new Date();

                if(hoy.before(user.getFin_penalizacion())){
                    throw new UsuarioPenalizadoException(user.getId());
                }

                //En caso de que ya se hubiese devuelto
                if(Prestamo.getFecha_devuelto() != null){
                    throw new PrestamoDevueltoException(Prestamo.getId_prestamo());
                }
                Prestamo.setFecha_prestado(new Date());
                return service.crearPrestamo(Prestamo);
            }).orElseThrow(() -> new PrestamoNotFoundException(id));


        return ResponseEntity.ok(ret);
    }
}


