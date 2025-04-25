package com.practica.practica.controller;

import java.util.List;
import java.util.Date;

import com.practica.practica.model.Libro;
import com.practica.practica.service.LibroService;
import com.practica.practica.service.PrestamoService;
import com.practica.practica.assembler.LibroModelAssembler;
import com.practica.practica.assembler.PrestamoModelAssembler;
import com.practica.practica.exceptions.LibroNotFoundException;
import com.practica.practica.exceptions.LibroNoDevueltoException;


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
@RequestMapping("/libros")
@AllArgsConstructor // Para generar un constructor con todos los argumentos
//maneja peticiones web
public class LibroController{

    private final LibroService service;
    private final PrestamoService prestamo_service;

    private PagedResourcesAssembler<Libro> pagedResourcesAssembler;
    private LibroModelAssembler usuarioModelAssembler;


    @PostMapping()
    ResponseEntity<Void> nuevoLibro(@RequestBody Libro nuevo){

        if(!service.existeLibroPorId(nuevo.getId())){
            Libro libro = service.crearLibro(nuevo);
            return ResponseEntity.created(linkTo(LibroController.class).slash(libro.getId()).toUri()).build();
        }

        return null;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Libro>> getLibros(
        @RequestParam(defaultValue = "",required=false)String contiene,
        @RequestParam(defaultValue = "",required=false) Boolean disponible,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "4", required = false) int size) {


        Page<Libro> libro = service.buscarLibros(contiene,disponible, page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(libro,usuarioModelAssembler));
    }

    @GetMapping(value="/{id}", produces = {"application/json"})
    public ResponseEntity<Libro> buscarLibro(@PathVariable int id){
        Libro libro = service.buscarPorId(id).orElseThrow(() -> new LibroNotFoundException(id));
        libro.add(linkTo(methodOn(LibroController.class).buscarLibro(id)).withSelfRel());
        return ResponseEntity.ok(libro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@Valid @RequestBody Libro newLibro, @PathVariable int id){
        Libro ret = service.buscarPorId(id).map(
            Libro -> {
                String new_titulo = newLibro.getTitulo();
                String new_autores = newLibro.getAutores();
                String new_edicion = newLibro.getEdicion();
                String new_editorial = newLibro.getEditorial();
                String new_isbn = newLibro.getIsbn();
                Integer new_copias = newLibro.getCopias();

                if(new_titulo != null) Libro.setTitulo(new_titulo);
                if(new_autores != null) Libro.setAutores(new_autores);
                if(new_edicion != null) Libro.setEdicion(new_edicion);
                if(new_editorial != null) Libro.setEditorial(new_editorial);
                if(new_isbn != null) Libro.setIsbn(new_isbn);
                if(new_copias != null) Libro.setCopias(new_copias);

                return service.crearLibro(Libro);
            }).orElseThrow(() -> new LibroNotFoundException(id));


        return ResponseEntity.ok(ret);
    }


    boolean estaPrestado(int id){
        return prestamo_service.buscarLibroPorId(id);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable int id){
        if(estaPrestado(id)){
            throw new LibroNoDevueltoException(id);
        }
        else if(service.existeLibroPorId(id)){
            service.eliminarLibro(id);
        }else{
            throw new LibroNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }


    
    
}

