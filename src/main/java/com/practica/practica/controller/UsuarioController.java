package com.practica.practica.controller;

import com.practica.practica.model.Usuario;
import com.practica.practica.service.UsuarioService;

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


    @PostMapping()
    ResponseEntity<Void> nuevoUsuario(@RequestBody Usuario nuevo){

        if(!service.existeUsuarioPorId(nuevo.getId())){
            Usuario user = service.crearUsuario(nuevo);
            return ResponseEntity.created(linkTo(UsuarioController.class).slash(user.getId()).toUri()).build();
            
        }

        return null;
    }
}
