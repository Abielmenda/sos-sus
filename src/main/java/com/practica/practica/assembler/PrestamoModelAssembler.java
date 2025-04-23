package com.practica.practica.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.practica.practica.controller.PrestamoController;
import com.practica.practica.model.Prestamo;

/**
 * This class extends RepresentationModelAssemblerSupport which is required for
 * Pagination.
 * It converts the Customer Entity to the Customer Model and has the code for it
 */
@Component
public class PrestamoModelAssembler extends RepresentationModelAssemblerSupport<Prestamo, Prestamo> {
    public PrestamoModelAssembler() {
        super(PrestamoController.class, Prestamo.class);
    }

    @Override
    public Prestamo toModel(Prestamo entity) {
        // entity.add(linkTo(methodOn(PrestamoController.class).buscarPrestamo(entity.getId_prestamo())).withSelfRel());
        return entity;
    }
}


