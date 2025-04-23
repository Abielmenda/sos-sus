package com.practica.practica.controller;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

import com.practica.practica.model.Usuario;
import com.practica.practica.model.Prestamo;
import com.practica.practica.service.PrestamoService;
import com.practica.practica.service.UsuarioService;
import com.practica.practica.assembler.UsuarioModelAssembler;
import com.practica.practica.exceptions.UsuarioNotFoundException;
import com.practica.practica.exceptions.UsuarioTienePrestamo;


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
import org.springframework.format.annotation.DateTimeFormat;


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
    private final PrestamoService prestamo_service;
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

    boolean tienePrestamo(int id){
        return prestamo_service.buscarUsuarioPorId(id);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id){
        if(tienePrestamo(id)){
            throw new UsuarioTienePrestamo(id);
        }
        else if(service.existeUsuarioPorId(id)){
            service.eliminarUsuario(id);
        }else{
            throw new UsuarioNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value="/{id}/prestamos/summary", produces = {"application/json"})
    public ResponseEntity<Map<String,Object>> summaryPrestamosDeUsuario(

        @PathVariable int id,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @RequestParam(required=false) Date start,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @RequestParam(required=false) Date end,

        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "4", required = false) int size){

        Usuario user = service.buscarPorId(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        if (start == null) start = new Date(0); 
        if (end == null) end = new Date(); 
        
        List<Prestamo> prestamos_activos = prestamo_service.buscarPrestamosActivosDeUsuario(id,start,end);
        List<Prestamo> prestamos_pasados = prestamo_service.buscarPrestamosDevueltosDeUsuario(id,start,end);

        for(Prestamo p : prestamos_activos){
            p.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(p.getId_prestamo())).withSelfRel());
        }

        for(Prestamo p : prestamos_pasados){
            p.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(p.getId_prestamo())).withSelfRel());
        }


        user.add(linkTo(methodOn(UsuarioController.class).buscarUsuario(id)).withSelfRel());
        Map<String, Object> response = new HashMap<>();

        response.put("usuario", user);
        response.put("prestamos_activos", prestamos_activos);
        response.put("prestamos_pasados", prestamos_pasados);

        return ResponseEntity.ok(response);

    }


    @GetMapping(value="/{id}/prestamos", produces = {"application/json"})
    public ResponseEntity<List<Prestamo>> buscarPrestamosActivosDeUsuario(

        @PathVariable int id,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "4", required = false) int size){

        Usuario user = service.buscarPorId(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        Date start = new Date(0);
        Date end = new Date();
        
        List<Prestamo> prestamos_activos = prestamo_service.buscarPrestamosActivosDeUsuario(id,start,end);

        for(Prestamo p : prestamos_activos){
            p.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(p.getId_prestamo())).withSelfRel());
        }

        return ResponseEntity.ok(prestamos_activos);

    }


    @GetMapping(value="/{id}/prestamos/history", produces = {"application/json"})
    public ResponseEntity<List<Prestamo>> buscarPrestamosPasadosDeUsuario(

        @PathVariable int id,
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "4", required = false) int size){

        Usuario user = service.buscarPorId(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        Date start = new Date(0);
        Date end = new Date();
        
        List<Prestamo> prestamos_devueltos = prestamo_service.buscarTodosPrestamosDevueltosDeUsuario(id,start,end);

        for(Prestamo p : prestamos_devueltos){
            p.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(p.getId_prestamo())).withSelfRel());
        }

        return ResponseEntity.ok(prestamos_devueltos);

    }









    
    
}
