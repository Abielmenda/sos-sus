package com.practica.practica.exceptions;

public class PrestamoNotFoundException extends RuntimeException{
    public PrestamoNotFoundException(int id){
        super("Prestamo " + id +" no encontrado");
    }
}

