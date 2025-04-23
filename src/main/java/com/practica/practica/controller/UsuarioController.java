package com.practica.practica.controller;

import java.util.List;
import java.util.Date;

import com.practica.practica.model.Usuario;
import com.practica.practica.service.UsuarioService;
import com.practica.practica.assembler.UsuarioModelAssembler;
import com.practica.practica.exceptions.UsuarioNotFoundException;

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
@RequestMapping("/usuarios")
@AllArgsConstructor // Para generar un constructor con todos los argumentos
//maneja peticiones web
public class UsuarioController{

    private final UsuarioService service;
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private UsuarioModelAssembler usuarioModelAssembler;


    @PostMapping()
    ResponseEntity<Void> nuevoUsuario(@RequestBody Usuario nuevo){

        if(!service.existeUsuarioPorId(nuevo.getId())){
            Usuario user = service.crearUsuario(nuevo);
            return ResponseEntity.created(linkTo(UsuarioController.class).slash(user.getId()).toUri()).build();
        }

        return null;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "4", required = false) int size) {


        Page<Usuario> usuarios = service.buscarUsuarios(page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(usuarios,usuarioModelAssembler));
    }

    @GetMapping(value="/{id}", produces = {"application/json"})
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable int id){
        Usuario user = service.buscarPorId(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        user.add(linkTo(methodOn(UsuarioController.class).buscarUsuario(id)).withSelfRel());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@Valid @RequestBody Usuario newUsuario, @PathVariable int id){
        Usuario ret = service.buscarPorId(id).map(
            Usuario -> {
                String new_nombre = newUsuario.getNombre();
                String new_matricula = newUsuario.getMatricula();
                Date new_fecha = newUsuario.getFecha_nacimiento();
                String new_correo = newUsuario.getCorreo_electronico();
                Date penalizacion = newUsuario.getFin_penalizacion();

                if(new_nombre != null) Usuario.setNombre(new_nombre);
                if(new_matricula != null) Usuario.setMatricula(new_matricula);
                if(new_fecha != null) Usuario.setFecha_nacimiento(new_fecha);
                if(new_correo != null) Usuario.setCorreo_electronico(new_correo);
                if(penalizacion != null) Usuario.setFin_penalizacion(penalizacion);


                return service.crearUsuario(Usuario);
            }).orElseThrow(() -> new UsuarioNotFoundException(id));


        return ResponseEntity.ok(ret);
    }


    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id){
        if(service.existeUsuarioPorId(id)){
            service.eliminarUsuario(id);
        }else{
            throw new UsuarioNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }


    
    
}
